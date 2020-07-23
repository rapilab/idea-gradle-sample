package com.phodal.gradal.plugins.gradle

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.ArrayUtil
import com.intellij.util.SystemProperties
import com.phodal.plugins.gradle.GradleProjectInfo
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.jetbrains.annotations.NotNull
import java.io.File

class GradleTasksExecutorImpl {
    @NotNull
    private fun getLogger(): Logger {
        return Logger.getInstance(GradleTasksExecutorImpl::class.java)
    }

    fun executeTask(project: Project, projectPath: String) {
        val isBuildWithGradle = GradleProjectInfo.isBuildWithGradle(project)

        val connector = GradleConnector.newConnector();
        connector.forProjectDirectory(File(projectPath));
        val connection: ProjectConnection = connector.connect()

        val operation: BuildLauncher = connection.newBuild()
        //        val operation: LongRunningOperation = connection.newBuild()

        val javaHome: String = SystemProperties.getJavaHome();
        operation.setJavaHome(File(javaHome))

        val logMessage = "Build command line options: clean, build"
        if (isBuildWithGradle) {
            val gradleTasks = arrayOf("clean", "build").toList()
            operation.forTasks(*ArrayUtil.toStringArray(gradleTasks))

            connection.use {
                operation.run();
            }
        }
        getLogger().info(logMessage)
    }

}
