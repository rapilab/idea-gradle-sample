package com.phodal.gradal.plugins.gradle.data.service

import com.google.common.annotations.VisibleForTesting
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.Key
import com.intellij.openapi.externalSystem.model.ProjectKeys
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable

class PGradleModuleModelDataService : ModuleModelDataService<GradleModuleModel>() {
    private var myModuleSetup: GradleModuleSetup

    private val PROCESSING_AFTER_BUILTIN_SERVICES = ProjectKeys.LIBRARY_DEPENDENCY.processingWeight + 1
    val GRADLE_MODULE_MODEL = Key.create(GradleModuleModel::class.java, PROCESSING_AFTER_BUILTIN_SERVICES)

    override fun getTargetDataKey(): Key<GradleModuleModel> {
        return GRADLE_MODULE_MODEL
    }

    init {
        myModuleSetup = GradleModuleSetup()
    }

    @VisibleForTesting
    fun GradleModuleModelDataService(moduleSetup: GradleModuleSetup) {
        myModuleSetup = moduleSetup
    }

    private fun populateExtraBuildParticipantFromBuildSrc(toImport: Collection<DataNode<GradleModuleModel>>, project: Project) {

    }

    override fun importData(toImport: Collection<DataNode<GradleModuleModel>>, project: Project, modelsProvider: IdeModifiableModelsProvider, modelsByModuleName: Map<String, GradleModuleModel>) {
        for (module in modelsProvider.modules) {
            val gradleModuleModel = modelsByModuleName[module.name]
            if (gradleModuleModel == null) {
                onModelNotFound(module, modelsProvider)
            } else {
                myModuleSetup.setUpModule(module, modelsProvider, gradleModuleModel)
            }
        }

        populateExtraBuildParticipantFromBuildSrc(toImport, project);

    }
}
