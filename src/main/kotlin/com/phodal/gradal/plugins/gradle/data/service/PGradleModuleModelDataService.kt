package com.phodal.gradal.plugins.gradle.data.service

import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.externalSystem.service.project.manage.ProjectDataService
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable

class PGradleModuleModelDataService : ProjectDataService<GradleModuleModel, Void> {
    private val PROCESSING_AFTER_BUILTIN_SERVICES = ProjectKeys.LIBRARY_DEPENDENCY.processingWeight + 1
    val GRADLE_MODULE_MODEL = Key.create(GradleModuleModel::class.java, PROCESSING_AFTER_BUILTIN_SERVICES)

    override fun getTargetDataKey(): Key<GradleModuleModel> {
        return GRADLE_MODULE_MODEL
    }

    init {
        println(".................------------")
    }

    override fun removeData(toRemove: Computable<MutableCollection<Void>>, toIgnore: MutableCollection<DataNode<GradleModuleModel>>, projectData: ProjectData, project: Project, modelsProvider: IdeModifiableModelsProvider) {
        TODO("Not yet implemented")
    }

    override fun computeOrphanData(toImport: MutableCollection<DataNode<GradleModuleModel>>, projectData: ProjectData, project: Project, modelsProvider: IdeModifiableModelsProvider): Computable<MutableCollection<Void>> {
        TODO("Not yet implemented")
    }

    override fun importData(toImport: MutableCollection<DataNode<GradleModuleModel>>, projectData: ProjectData?, project: Project, modelsProvider: IdeModifiableModelsProvider) {
        TODO("Not yet implemented")
    }
}
