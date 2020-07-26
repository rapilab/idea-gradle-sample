package com.phodal.gradal.plugins.gradle

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskType
import com.intellij.openapi.project.Project
import com.phodal.gradal.plugins.gradle.data.service.GradleUtil.GRADLE_SYSTEM_ID
import org.gradle.tooling.BuildAction
import java.io.File
import java.util.*

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
        request
                .setJvmArguments(jvmArguments)
                .setCommandLineArguments()
                .setBuildAction(buildAction)

        val executor: GradleTasksExecutor = GradleTasksExecutorImpl(request)
        executor.queue()
    }

    class Request @JvmOverloads constructor(val myProject: Project,
                                            private val path: File,
                                            gradleTasks: List<String>,
                                            taskId: ExternalSystemTaskId = ExternalSystemTaskId.create(GRADLE_SYSTEM_ID, ExternalSystemTaskType.EXECUTE_TASK, myProject)) {
        private var myBuildAction: BuildAction<*>? = null
        private val myGradleTasks: List<String>
        private val myJvmArguments: MutableList<String>
        private val myCommandLineArguments: List<String>
        private val myEnv: Map<String, String>
        private val myTaskId: ExternalSystemTaskId
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
    }
}
