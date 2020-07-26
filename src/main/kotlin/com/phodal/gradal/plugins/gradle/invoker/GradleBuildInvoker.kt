package com.phodal.gradal.plugins.gradle.invoker

import com.intellij.build.BuildConsoleUtils
import com.intellij.build.BuildEventDispatcher
import com.intellij.build.BuildViewManager
import com.intellij.build.DefaultBuildDescriptor
import com.intellij.build.events.FailureResult
import com.intellij.build.events.impl.FinishBuildEventImpl
import com.intellij.build.events.impl.SkippedResultImpl
import com.intellij.build.events.impl.StartBuildEventImpl
import com.intellij.build.events.impl.SuccessResultImpl
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.externalSystem.model.task.*
import com.intellij.openapi.externalSystem.model.task.event.ExternalSystemBuildEvent
import com.intellij.openapi.externalSystem.model.task.event.ExternalSystemTaskExecutionEvent
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemEventDispatcher
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import com.phodal.gradal.plugins.gradle.GradleUtil.GRADLE_SYSTEM_ID
import org.gradle.tooling.BuildAction
import java.io.File
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class GradleBuildInvoker(project: Project) {
    private var myProject: Project = project
    private val ASSEMBLE = "assemble"

    companion object {
        fun getInstance(project: Project): GradleBuildInvoker {
            return ServiceManager.getService(project, GradleBuildInvoker::class.java)
        }
    }

    fun assemble(projectPath: String, buildAction: BuildAction<*>?) {
        val gradleTasks = arrayOf(ASSEMBLE).toList()
        val myProjectPath = File(projectPath)

        val request = Request(myProject, myProjectPath, gradleTasks)

        executeTaskRequest(request, buildAction)
    }


    private fun executeTaskRequest(request: Request, buildAction: BuildAction<*>?) {
        val jvmArguments: List<String> = ArrayList()

        val buildTaskListener = createBuildTaskListener(request, "Build")
        request
                .setJvmArguments(jvmArguments)
                .setCommandLineArguments()
                .setBuildAction(buildAction)
                .setTaskListener(buildTaskListener);

        executeTasks(request)
    }

    private fun executeTasks(request: Request) {
        val executor: GradleTasksExecutor = GradleTasksExecutorImpl(request)
        executor.queue()
    }

    fun createBuildTaskListener(request: Request, executionName: String): ExternalSystemTaskNotificationListener {
        val buildViewManager = ServiceManager.getService(myProject, BuildViewManager::class.java)
        // This is resource is closed when onEnd is called or an exception is generated in this function bSee b/70299236.
        // We need to keep this resource open since closing it causes BuildOutputInstantReaderImpl.myThread to stop, preventing parsers to run.
        val eventDispatcher: BuildEventDispatcher = ExternalSystemEventDispatcher(request.myTaskId, buildViewManager)
        return try {
            object : ExternalSystemTaskNotificationListenerAdapter() {
                private var myBuildEventDispatcher = eventDispatcher
                private var myBuildFailed = false
                override fun onStart(id: ExternalSystemTaskId, workingDir: String) {
//                    val restartAction: AnAction = object : AnAction() {
//                        override fun update(e: AnActionEvent) {
//                            e.presentation.isEnabled = !myBuildStopper.contains(id)
//                        }
//
//                        override fun actionPerformed(e: AnActionEvent) {
//                            myBuildFailed = false
//                            // Recreate the reader since the one created with the listener can be already closed (see b/73102585)
//                            myBuildEventDispatcher.close()
//                            myBuildEventDispatcher = ExternalSystemEventDispatcher(request.myTaskId, buildViewManager)
//                            executeTasks(request)
//                        }
//                    }
//                    myBuildFailed = false
//                    val presentation = restartAction.templatePresentation
//                    presentation.setText("Restart")
//                    presentation.description = "Restart"
//                    presentation.setIcon(AllIcons.Actions.Compile)
                    val eventTime = System.currentTimeMillis()
                    val event = StartBuildEventImpl(DefaultBuildDescriptor(id, executionName, workingDir, eventTime),
                            "running...")
//                    event.withRestartAction(restartAction).withExecutionFilter(AndroidReRunBuildFilter(workingDir))
//                    if (BuildAttributionUtil.isBuildAttributionEnabledForProject(myProject)) {
//                        event.withExecutionFilter(BuildAttributionOutputLinkFilter())
//                    }
                    myBuildEventDispatcher.onEvent(id, event)
                }

                override fun onStatusChange(event: ExternalSystemTaskNotificationEvent) {
                    if (event is ExternalSystemBuildEvent) {
                        val buildEvent = event.buildEvent
                        myBuildEventDispatcher.onEvent(event.getId(), buildEvent)
                    } else if (event is ExternalSystemTaskExecutionEvent) {
                        val buildEvent = ExternalSystemUtil.convert(event)
                        myBuildEventDispatcher.onEvent(event.getId(), buildEvent)
                    }
                }

                override fun onTaskOutput(id: ExternalSystemTaskId, text: String, stdOut: Boolean) {
                    myBuildEventDispatcher.setStdOut(stdOut)
                    myBuildEventDispatcher.append(text)
                }

                override fun onEnd(id: ExternalSystemTaskId) {
                    val eventDispatcherFinished = CountDownLatch(1)
                    myBuildEventDispatcher.invokeOnCompletion { t: Throwable? ->
//                        if (myBuildFailed) {
//                            ServiceManager.getService(myProject, BuildOutputParserManager::class.java).sendBuildFailureMetrics()
//                        }
                        eventDispatcherFinished.countDown()
                    }
                    myBuildEventDispatcher.close()

                    // The underlying output parsers are closed asynchronously. Wait for completion in tests.
                    if (ApplicationManager.getApplication().isUnitTestMode) {
                        try {
                            eventDispatcherFinished.await(10, TimeUnit.SECONDS)
                        } catch (ex: InterruptedException) {
                            throw RuntimeException("Timeout waiting for event dispatcher to finish.", ex)
                        }
                    }
                }

                override fun onSuccess(id: ExternalSystemTaskId) {
                    addBuildAttributionLinkToTheOutput(id)
                    val event = FinishBuildEventImpl(id, null, System.currentTimeMillis(), "finished",
                            SuccessResultImpl())
                    myBuildEventDispatcher.onEvent(id, event)
                }

                private fun addBuildAttributionLinkToTheOutput(id: ExternalSystemTaskId) {
//                    if (BuildAttributionUtil.isBuildAttributionEnabledForProject(myProject)) {
//                        val buildAttributionTabLinkLine: String = BuildAttributionUtil.buildOutputLine()
//                        onTaskOutput(id, """
//
//     $buildAttributionTabLinkLine
//     """.trimIndent(), true)
//                    }
                }

                override fun onFailure(id: ExternalSystemTaskId, e: Exception) {
                    myBuildFailed = true
                    val title = "$executionName failed"
                    val dataProvider = BuildConsoleUtils.getDataProvider(id, buildViewManager)
                    val failureResult: FailureResult = ExternalSystemUtil.createFailureResult(title, e, GRADLE_SYSTEM_ID, myProject, dataProvider)
                    myBuildEventDispatcher.onEvent(id, FinishBuildEventImpl(id, null, System.currentTimeMillis(), "failed", failureResult))
                }

                override fun onCancel(id: ExternalSystemTaskId) {
                    super.onCancel(id)
                    // Cause build view to show as skipped all pending tasks (b/73397414)
                    val event = FinishBuildEventImpl(id, null, System.currentTimeMillis(), "cancelled", SkippedResultImpl())
                    myBuildEventDispatcher.onEvent(id, event)
                    myBuildEventDispatcher.close()
                }
            }
        } catch (exception: Exception) {
            eventDispatcher.close()
            throw exception
        }
    }

    class Request @JvmOverloads constructor(val myProject: Project,
                                            private val path: File,
                                            gradleTasks: List<String>,
                                            taskId: ExternalSystemTaskId = ExternalSystemTaskId.create(GRADLE_SYSTEM_ID, ExternalSystemTaskType.EXECUTE_TASK, myProject)) {
        lateinit var myTaskListener: ExternalSystemTaskNotificationListener
        private var myBuildAction: BuildAction<*>? = null
        private val myGradleTasks: List<String>
        private val myJvmArguments: MutableList<String>
        private val myCommandLineArguments: List<String>
        private val myEnv: Map<String, String>
        val myTaskId: ExternalSystemTaskId
        val myBuildFilePath: File

        init {
            myGradleTasks = ArrayList(gradleTasks)
            myJvmArguments = ArrayList()
            myCommandLineArguments = ArrayList()
            myTaskId = taskId
            myBuildFilePath = path
            myEnv = LinkedHashMap()
        }

        fun getGradleTasks(): List<String?> {
            return myGradleTasks
        }

        fun setJvmArguments(jvmArguments: List<String>): Request {
            myJvmArguments.clear()
            myJvmArguments.addAll(jvmArguments)
            return this
        }

        fun setCommandLineArguments(): Request {
            return this
        }

        fun setBuildAction(buildAction: BuildAction<*>?): Request {
            this.myBuildAction = buildAction
            return this
        }

        fun getBuildAction(): BuildAction<*>? {
            return this.myBuildAction
        }

        fun setTaskListener(buildTaskListener: ExternalSystemTaskNotificationListener): Request {
            this.myTaskListener = buildTaskListener
            return this
        }
    }
}
