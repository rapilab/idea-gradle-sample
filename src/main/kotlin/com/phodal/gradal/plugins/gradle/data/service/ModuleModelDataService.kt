/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phodal.gradal.plugins.gradle.data.service

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.project.ModuleData
import com.intellij.openapi.externalSystem.model.project.ProjectData
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import java.util.*

abstract class ModuleModelDataService<T : ModuleModel?> : AbstractProjectDataService<T, Void?>() {
    override fun importData(toImport: Collection<DataNode<T>>,
                            projectData: ProjectData?,
                            project: Project,
                            modelsProvider: IdeModifiableModelsProvider) {
        if (toImport.isEmpty()) {
            // there can be other build systems which can use the same project elements for the import
            if (projectData != null && GradleUtil.GRADLE_SYSTEM_ID == projectData.owner) {
                onModelsNotFound(modelsProvider)
            }
            return
        }
        try {
            importData(toImport, project, modelsProvider)
        } catch (e: Throwable) {
//      String msg = e.getMessage();
//      GradleSyncState.getInstance(project).syncFailed(isNotEmpty(msg) ? msg : e.getClass().getCanonicalName(), e, null);
        }
    }

    private fun onModelsNotFound(modelsProvider: IdeModifiableModelsProvider) {
        for (module in modelsProvider.modules) {
            onModelNotFound(module, modelsProvider)
        }
    }

    protected open fun onModelNotFound(module: Module, modelsProvider: IdeModifiableModelsProvider) {}

    private fun importData(toImport: Collection<DataNode<T>>,
                           project: Project,
                           modelsProvider: IdeModifiableModelsProvider) {
        WriteCommandAction.runWriteCommandAction(project) {
            if (project.isDisposed) {
                return@runWriteCommandAction
            }
            val modelsByModuleName = indexByModuleName(toImport, modelsProvider)
            importData(toImport, project, modelsProvider, modelsByModuleName)
        }
    }

    protected abstract fun importData(toImport: Collection<DataNode<T>>,
                                      project: Project,
                                      modelsProvider: IdeModifiableModelsProvider,
                                      modelsByModuleName: Map<String, T>)

    private fun indexByModuleName(dataNodes: Collection<DataNode<T>>,
                                  modelsProvider: IdeModifiableModelsProvider): Map<String, T> {
        if (dataNodes.isEmpty()) {
            return emptyMap()
        }
        val index: MutableMap<String, T> = HashMap()
        for (dataNode in dataNodes) {
            val model = dataNode.data
            var moduleName = model!!.moduleName
            if (dataNode.parent != null) {
                val moduleData = dataNode.parent!!.data as ModuleData
                val module = modelsProvider.findIdeModule(moduleData)
                if (module != null && module.name != moduleName) {
                    // If the module name in modelsProvider is different from in moduleData, use module name in modelsProvider as key.
                    // This happens when there are multiple *iml files for one module, which can be caused by opening a project created on a different machine,
                    // or opening projects with both Intellij and Studio, or moving existing module to different locations.
                    moduleName = module.name
                }
            }
            index[moduleName] = model
        }
        return index
    }

    protected val log: Logger
        protected get() = Logger.getInstance(javaClass)
}
