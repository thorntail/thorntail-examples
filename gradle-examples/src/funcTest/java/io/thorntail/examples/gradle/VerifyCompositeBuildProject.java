package io.thorntail.examples.gradle;

import org.gradle.testkit.runner.GradleRunner;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VerifyCompositeBuildProject extends BaseExamplesTest {

    private static final String BUILD_FILE_PATH = "composite-build/build.gradle";

    @Test
    public void testTasks() {
        Path path = Paths.get(System.getProperty("user.dir"), BUILD_FILE_PATH);
        GradleRunner runner = constructRunnerFor(path.toFile());
        runner.withArguments("clean", "build").build();
    }

}
