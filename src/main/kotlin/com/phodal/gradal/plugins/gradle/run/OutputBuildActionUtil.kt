package com.phodal.gradal.plugins.gradle.run

import com.intellij.openapi.module.Module
import com.phodal.gradal.plugins.gradle.facet.GradleFacet
import org.jetbrains.annotations.NotNull

class OutputBuildActionUtil {
    companion object {
        fun create(modules: Array<@NotNull Module>): OutputBuildAction? {
            return OutputBuildAction(getModuleGradlePaths(modules))
        }

        private fun getModuleGradlePaths(modules: Array<@NotNull Module>): Set<String> {
            val gradlePaths = mutableSetOf<String>()
            modules.mapNotNullTo(gradlePaths) {
//                val facet = FacetManager.getInstance(it).getFacetByType(GradleFacet.facetTypeId)
                val facet = GradleFacet.getInstance(it)
                facet?.configuration?.GRADLE_PROJECT_PATH
            }
            return gradlePaths
        }

    }
}
