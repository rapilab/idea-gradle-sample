package com.phodal.gradal.plugins.gradle

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationEvent
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListenerAdapter
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.ui.MessageType
import com.intellij.util.ArrayUtil
import com.intellij.util.Function
import com.intellij.util.SystemProperties
import net.jcip.annotations.GuardedBy
import org.gradle.tooling.*
import org.jetbrains.annotations.NotNull
import org.jetbrains.plugins.gradle.service.execution.GradleExecutionHelper
import org.jetbrains.plugins.gradle.settings.DistributionType
import org.jetbrains.plugins.gradle.settings.GradleExecutionSettings
import java.io.File


class GradleTasksExecutorImpl(request: GradleBuildInvoker.Request) : GradleTasksExecutor(request.myProject) {
    private lateinit var myProgressIndicator: ProgressIndicator
    private var myRequest: GradleBuildInvoker.Request = request

    private val myHelper: GradleExecutionHelper = GradleExecutionHelper()

    @GuardedBy("myCompletionLock")
    private var myCompletionCounter = 0
    private val myCompletionLock = Object()

    @NotNull
    private fun getLogger(): Logger {
        return Logger.getInstance(GradleTasksExecutorImpl::class.java)
    }

    override fun run(indicator: ProgressIndicator) {
        myProgressIndicator = indicator

        getLogger().info("Running")

        invokeGradleTasks()

        myProgressIndicator.stop()
    }

    private fun invokeGradleTasks() {
        val isBuildWithGradle = GradleProjectInfo.isBuildWithGradle(myRequest.myProject)
        val connector = GradleConnector.newConnector();
        connector.forProjectDirectory(myRequest.myBuildFilePath);

        val executionSettings = GradleExecutionSettings(null, null, DistributionType.LOCAL, null, false)

        val executeTasksFunction: Function<ProjectConnection, Void> = Function { connection ->
            val executingTasksText = "Executing tasks: " + myRequest.getGradleTasks() + " in project " + myRequest.myBuildFilePath.path
            addToEventLog(executingTasksText, MessageType.INFO)
            val output = StringBuilder()

            val id = myRequest.myTaskId
            val taskListener: ExternalSystemTaskNotificationListener = myRequest.myTaskListener
            taskListener.onStart(id, myRequest.myBuildFilePath.path)
            taskListener.onTaskOutput(id, executingTasksText + SystemProperties.getLineSeparator() + SystemProperties.getLineSeparator(), true)

            val buildAction: BuildAction<*>? = myRequest.getBuildAction()
            val operation: LongRunningOperation;

            if (buildAction != null) {
                operation = connection.action(buildAction)
            } else {
                operation = connection.newBuild()
            }

            GradleExecutionHelper.prepare(operation, id, executionSettings, object : ExternalSystemTaskNotificationListenerAdapter() {
                override fun onStatusChange(event: ExternalSystemTaskNotificationEvent) {
//                    if (myBuildStopper.contains(id)) {
                        taskListener.onStatusChange(event)
//                    }
                }

                override fun onTaskOutput(id: ExternalSystemTaskId, text: String, stdOut: Boolean) {
                    output.append(text)
//                    if (myBuildStopper.contains(id)) {
                        taskListener.onTaskOutput(id, text, stdOut)
//                    }
                }
            }, connection)

            val javaHome: String = SystemProperties.getJavaHome();
            operation.setJavaHome(File(javaHome))


            if (isBuildWithGradle) {
                (operation as BuildActionExecuter<*>).forTasks(*ArrayUtil.toStringArray(myRequest.getGradleTasks()))
                connection.use {
                    operation.run()
                }
            }
            val logMessage = "Build command line options:" + myRequest.getGradleTasks()
            getLogger().info(logMessage)

            taskListener.onSuccess(id)
            taskListener.onEnd(id)
            null
        }

        myHelper.execute(myRequest.myBuildFilePath.path, executionSettings,
                myRequest.myTaskId, myRequest.myTaskListener, null, executeTasksFunction)
    }

    private fun addToEventLog(message: String, type: MessageType) {
        LOGGING_NOTIFICATION.createNotification(message, type).notify(myProject)
    }

    override fun queueAndWaitForCompletion() {
        var counterBefore: Int
        synchronized(myCompletionLock) { counterBefore = myCompletionCounter }
        queue()
        synchronized(myCompletionLock) {
            while (true) {
                if (myCompletionCounter > counterBefore) {
                    break
                }
                try {
                    myCompletionLock.wait()
                } catch (e: InterruptedException) {
                    // Just stop waiting.
                    break
                }
            }
        }
    }


    override fun onSuccess() {
        super.onSuccess()
        onCompletion()
    }

    override fun onCancel() {
        super.onCancel()
        onCompletion()
    }

    private fun onCompletion() {
        synchronized(myCompletionLock) {
            myCompletionCounter++
            myCompletionLock.notifyAll()
        }
    }

}
