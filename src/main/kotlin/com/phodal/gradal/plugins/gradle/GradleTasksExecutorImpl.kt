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
import java.util.concurrent.atomic.AtomicReference


class GradleTasksExecutorImpl {
    @NotNull
    private fun getLogger(): Logger {
        return Logger.getInstance(GradleTasksExecutorImpl::class.java)
    }

    fun executeTask(project: Project, projectPath: String, buildAction: BuildAction<*>?) {
        val gradleTasks = arrayOf("clean", "build").toList()
        val myProjectPath = File(projectPath)

        val request = GradleBuildInvoker.Request(project, myProjectPath, gradleTasks)

        executeTaskRequest(request, buildAction)
    }

    private fun executeTaskRequest(request: GradleBuildInvoker.Request, buildAction: BuildAction<*>?) {
        val jvmArguments: List<String> = ArrayList()
//        val commandLineArguments: List<String> = ArrayList()

//        val buildTaskListener: ExternalSystemTaskNotificationListener = createBuildTaskListener(request, "Build")

        request
                .setJvmArguments(jvmArguments)
                .setCommandLineArguments()
                .setBuildAction(buildAction)

        executeTask(request)
    }

    private fun executeTask(request: GradleBuildInvoker.Request) {
        val isBuildWithGradle = GradleProjectInfo.isBuildWithGradle(request.myProject)
        val connector = GradleConnector.newConnector();
        connector.forProjectDirectory(request.myBuildFilePath);
        val connection: ProjectConnection = connector.connect()

        val buildAction: BuildAction<*>? = request.getBuildAction()
        val operation: LongRunningOperation;

        if (buildAction != null) {
            operation =  connection.action(buildAction)
        } else {
            operation = connection.newBuild()
        }

        val javaHome: String = SystemProperties.getJavaHome();
        operation.setJavaHome(File(javaHome))

        val model = AtomicReference<Any?>(null)

        val logMessage = "Build command line options: clean, build"
        if (isBuildWithGradle) {
            (operation as BuildActionExecuter<*>).forTasks(*ArrayUtil.toStringArray(request.getGradleTasks()))

            connection.use {
                model.set(operation.run())
            }
        }
        getLogger().info(logMessage)
    }
}
