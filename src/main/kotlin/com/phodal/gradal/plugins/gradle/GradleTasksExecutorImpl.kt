package com.phodal.gradal.plugins.gradle

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.ArrayUtil
import com.intellij.util.SystemProperties
import com.phodal.plugins.gradle.GradleProjectInfo
import org.gradle.tooling.*
import org.jetbrains.annotations.NotNull
import java.io.File
import java.util.*

class GradleTasksExecutorImpl {
//    @Volatile
//    private var myProgressIndicator: ProgressIndicator = EmptyProgressIndicator()

    @NotNull
    private fun getLogger(): Logger {
        return Logger.getInstance(GradleTasksExecutorImpl::class.java)
    }

    fun executeTask(project: Project, projectPath: String) {
        val gradleTasks = arrayOf("clean", "build").toList()
        val myProjectPath = File(projectPath)

        val request = GradleBuildInvoker.Request(project, myProjectPath, gradleTasks)

        executeTaskRequest(request)
    }

    private fun executeTaskRequest(request: GradleBuildInvoker.Request) {
        val jvmArguments: List<String> = ArrayList()
        val commandLineArguments: List<String> = ArrayList()

//        val buildTaskListener: ExternalSystemTaskNotificationListener = createBuildTaskListener(request, "Build")

        request
                .setJvmArguments(jvmArguments)
                .setCommandLineArguments(commandLineArguments)

        executeTask(request)
    }

    private fun executeTask(request: GradleBuildInvoker.Request) {
        val isBuildWithGradle = GradleProjectInfo.isBuildWithGradle(request.myProject)
        val connector = GradleConnector.newConnector();
        connector.forProjectDirectory(request.myBuildFilePath);
        val connection: ProjectConnection = connector.connect()

        val operation: LongRunningOperation = connection.newBuild()
        val javaHome: String = SystemProperties.getJavaHome();
        operation.setJavaHome(File(javaHome))

        val logMessage = "Build command line options: clean, build"
        if (isBuildWithGradle) {
            (operation as BuildLauncher).forTasks(*ArrayUtil.toStringArray(request.getGradleTasks()))

            connection.use {
                operation.run();
            }
        }
        getLogger().info(logMessage)
    }

}
