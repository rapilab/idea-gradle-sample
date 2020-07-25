package com.phodal.gradal.plugins.gradle

import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskType
import com.intellij.openapi.project.Project
import org.gradle.tooling.BuildAction
import org.jetbrains.annotations.NonNls
import java.io.File
import java.util.*

object GradleBuildInvoker {
    @NonNls
    val SYSTEM_ID = ProjectSystemId("GRADLE")
    val GRADLE_SYSTEM_ID = SYSTEM_ID

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
