package com.phodal.gradal.plugins.gradle.run

import com.intellij.openapi.module.Module
import com.phodal.gradal.plugins.gradle.facet.GradleFacet

class OutputBuildActionUtil {
    companion object {
        fun create(modules: Array<Module>): OutputBuildAction? {
            val moduleGradlePaths = getModuleGradlePaths(modules)

            val path = arrayOf("/Users/fdhuang/IntelliJIDEAProjects/MyApplication", "/Users/fdhuang/IntelliJIDEAProjects/MyApplication/app/app.iml")
            path.toSet()
            return OutputBuildAction(path.toSet())
        }

        private fun getModuleGradlePaths(modules: Array<Module>): Set<String> {
            val gradlePaths = mutableSetOf<String>()
            modules.mapNotNullTo(gradlePaths) {
                val facet = GradleFacet.getInstance(it)
                if (facet != null) {
                    facet.configuration?.GRADLE_PROJECT_PATH
                } else {
                    it.moduleFilePath
                }
            }
            return gradlePaths
        }

    }
}
