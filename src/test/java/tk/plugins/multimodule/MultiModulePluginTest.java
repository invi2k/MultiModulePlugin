package tk.plugins.multimodule;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MultiModulePluginTest {
  private static final String PLUGIN_NAME = "tk.plugins.multimodule";

  @Test
  public void applyTest() {
    Project project = ProjectBuilder.builder().build();
    assertNotNull(project);
    project.getPluginManager().apply(PLUGIN_NAME);
    assertTrue(project.getPluginManager().hasPlugin(PLUGIN_NAME));
  }
}
