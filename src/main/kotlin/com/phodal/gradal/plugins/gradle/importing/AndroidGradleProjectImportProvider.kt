/*
 * Copyright (C) 2018 The Android Open Source Project
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
package com.phodal.gradal.plugins.gradle.importing

import com.intellij.openapi.module.ModifiableModuleModel
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.packaging.artifacts.ModifiableArtifactModel
import com.intellij.projectImport.ProjectImportBuilder
import com.intellij.projectImport.ProjectImportProvider
import icons.PluginIcons
import javax.swing.Icon

class AndroidGradleProjectImportProvider : ProjectImportProvider() {
    override fun doGetBuilder(): ProjectImportBuilder<*> {
        return ProjectImportBuilder.EXTENSIONS_POINT_NAME.findExtensionOrFail(AndroidGradleImportBuilder::class.java)
    }

    class AndroidGradleImportBuilder : ProjectImportBuilder<String>() {
        override fun getName(): String {
            return "Android Gradle"
        }

        override fun getIcon(): Icon {
            return PluginIcons.Console.Python
        }

        override fun getList(): List<String> {
            return emptyList()
        }

        override fun isMarked(element: String): Boolean {
            return false
        }

        @Throws(ConfigurationException::class)
        override fun setList(list: List<String>) {
        }

        override fun setOpenProjectSettingsAfter(on: Boolean) {}
        override fun commit(project: Project,
                            model: ModifiableModuleModel,
                            modulesProvider: ModulesProvider,
                            artifactModel: ModifiableArtifactModel): List<Module>? {
            GradleProjectImporter.getInstance().importProjectCore(project.baseDir)
            return emptyList()
        }
    }
}
