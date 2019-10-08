package io.thorntail.examples.gradle;

import org.gradle.testkit.runner.GradleRunner;
import org.junit.Assert;

import java.io.File;

/**
 * Base class for executing an example projects in an automatecd
 */
abstract class BaseExamplesTest {

    /**
     * Construct the Gradle runner pointing to the given build script. This method configures the runner with common
     * parameters which are needed for all builds.
     *
     * @param buildScript the location of the build script.
     * @return the GradleRunner instance that can be used for executing a build.
     */
    GradleRunner constructRunnerFor(File buildScript) {
        Assert.assertTrue("Unable to locate build script: " + buildScript.getAbsolutePath(), buildScript.exists());
        String thorntailVersion = System.getProperty("VERSION_THORNTAIL");
        System.out.println("Verifying the build against Thorntail version: " + thorntailVersion);
        return GradleRunner.create().withProjectDir(buildScript.getParentFile())
                .withArguments("-s", "-i", String.format("-DthorntailVersion=%s", thorntailVersion))
                .forwardOutput();
    }
}
