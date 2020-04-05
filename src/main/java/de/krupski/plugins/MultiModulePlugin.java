package de.krupski.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiModulePlugin implements Plugin<Project> {
  private List<String> modules = new ArrayList<>();

  @Override
  public void apply(Project project) {
    MultiModulePluginExtension extension = project.getExtensions()
            .create("modules", MultiModulePluginExtension.class);

    if (extension.modules != null) {
      Collections.addAll(modules, extension.modules);
    }
    try {
      File settings = new File("settings.gradle");
      if (settings.exists() && settings.isFile()) {
        readGradleSettings(settings);
        for (String module : modules) {
          createModule(module, settings);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readGradleSettings(File settings) throws FileNotFoundException {
    try (Scanner scanner = new Scanner(settings)) {
      Pattern pattern = Pattern.compile("^include ['\"](.*?)['\"].*?$");
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
          modules.remove(matcher.group(1));
        }
      }
    }
  }

  private void createModule(String moduleName, File settings) throws IOException {
    File moduleMainJava = new File(combinePath(moduleName, "src", "main", "java"));
    moduleMainJava.mkdirs();
    File moduleMainRes = new File(combinePath(moduleName, "src", "main", "resources"));
    moduleMainRes.mkdirs();
    File moduleTestJava = new File(combinePath(moduleName, "src", "test", "java"));
    moduleTestJava.mkdirs();
    File moduleTestRes = new File(combinePath(moduleName, "src", "test", "resources"));
    moduleTestRes.mkdirs();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(settings, true))) {
      writer.append(String.format("include '%s'", moduleName));
      writer.newLine();
    }
  }

  private static String combinePath(String... paths) {
    StringBuilder combinedPath = new StringBuilder();
    for (String path : paths) {
      if (combinedPath.length() > 0) {
        combinedPath.append(System.getProperty("file.separator"));
      }
      combinedPath.append(path);
    }
    return combinedPath.toString();
  }
}
