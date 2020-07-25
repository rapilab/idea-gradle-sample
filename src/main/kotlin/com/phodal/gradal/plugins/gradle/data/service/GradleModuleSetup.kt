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

import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.module.Module
import com.phodal.gradal.plugins.gradle.data.Facets.findFacet
import com.phodal.gradal.plugins.gradle.facet.GradleFacet
import com.phodal.gradal.plugins.gradle.facet.GradleFacetType
import org.gradle.tooling.model.GradleProject
import org.gradle.tooling.model.gradle.GradleScript
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jetbrains.kotlin.kapt.idea.KaptGradleModel
import java.io.IOException

class GradleModuleSetup {
//    fun setUpModule(module: Module,
//                    ideModelsProvider: IdeModifiableModelsProvider,
//                    models: GradleModuleModels): GradleModuleModel {
//        var gradleModuleModel: GradleModuleModel = GradleModuleSetup.createGradleModel(module, models)
//        setUpModule(module, ideModelsProvider, gradleModuleModel)
//        return gradleModuleModel
//    }

    fun setUpModule(module: Module,
                    ideModelsProvider: IdeModifiableModelsProvider,
                    model: GradleModuleModel) {
        var facet: @Nullable GradleFacet? = findFacet(module, ideModelsProvider, GradleFacet.getFacetTypeId())
        if (facet == null) {
            val facetModel = ideModelsProvider.getModifiableFacetModel(module)
            val facetType: GradleFacetType = GradleFacet.facetType
            facet = facetType.createFacet(module, GradleFacet.facetName, facetType.createDefaultConfiguration(), null)
            facetModel.addFacet(facet)
        }
        facet.setGradleModuleModel(model)
//        val gradleVersion: String = model.getGradlePluginseVersion()
//        if (StringUtil.isNotEmpty(gradleVersion)) {
//            GradleSyncState.getInstance(module.project).setLastSyncedGradleVersion(GradleVersion.parse(gradleVersion))
//        }
    }


    companion object {
        @NotNull
        private fun createGradleModel(@NotNull module: Module, @NotNull models: GradleModuleModels): GradleModuleModel {
            val gradleProject = models.findModel(GradleProject::class.java)!!

            var buildScript: GradleScript? = null
            try {
                buildScript = gradleProject.buildScript
            } catch (e: Throwable) {
            }
            val buildFilePath = buildScript?.sourceFile
            val agpVersion: String? = null;

            return GradleModuleModel(module.name, gradleProject, getGradlePlugins(models), buildFilePath, GradleModuleSetup.getGradleVersion(module),
                    agpVersion, models.findModel(KaptGradleModel::class.java))
        }
        private fun getGradlePlugins(models: GradleModuleModels): List<String> {
            return emptyList<String>()
        }

        // Retrieve Gradle version from wrapper file.
        private fun getGradleVersion(module: Module): String? {
            val gradleWrapper = GradleWrapper.find(module.project)
            return if (gradleWrapper != null) {
                try {
                    gradleWrapper.gradleFullVersion
                } catch (ignore: IOException) {
                    null
                }
            } else null
        }
    }
}
