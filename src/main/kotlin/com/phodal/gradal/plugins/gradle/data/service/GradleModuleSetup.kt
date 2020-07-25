package com.phodal.gradal.plugins.gradle.data.service

import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.module.Module

class GradleModuleSetup {
    fun setUpModule(module: Module,
                    ideModelsProvider: IdeModifiableModelsProvider,
                    models: GradleModuleModels): GradleModuleModel {
        return GradleModuleModel()
    }
}
