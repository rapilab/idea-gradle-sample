package com.phodal.gradal.plugins.gradle

import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import java.util.*

class GradleProjectInfo {
    fun getInstance(project: Project): GradleProjectInfo {
        return ServiceManager.getService(project, GradleProjectInfo::class.java)
    }

    companion object {
        fun isBuildWithGradle(myProject: Project): Boolean {
            return ReadAction.compute<Boolean, RuntimeException> {
                if (myProject.isDisposed()) {
                    return@compute false
                }
                if (Arrays.stream(ModuleManager.getInstance(myProject).modules)
                                .anyMatch { it: Module? -> ExternalSystemApiUtil.isExternalSystemAwareModule(ProjectSystemId("GRADLE"), it) }) {
                    return@compute true
                }

                return@compute false
            }
        }
    }
}
