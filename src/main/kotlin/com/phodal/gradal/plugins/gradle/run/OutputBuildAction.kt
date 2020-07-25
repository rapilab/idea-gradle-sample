package com.phodal.gradal.plugins.gradle.run

import com.google.common.collect.ImmutableSet
import com.phodal.gradal.plugins.gradle.run.OutputBuildAction.PostBuildProjectModels
import org.gradle.tooling.BuildAction
import org.gradle.tooling.BuildController
import org.gradle.tooling.model.GradleProject
import org.jetbrains.annotations.TestOnly
import java.io.Serializable
import java.util.*

class OutputBuildAction internal constructor(moduleGradlePaths: Collection<String>) : BuildAction<PostBuildProjectModels?>, Serializable {
    private val myGradlePaths: ImmutableSet<String> = ImmutableSet.copyOf(moduleGradlePaths)

    @TestOnly
    fun getMyGradlePaths(): Collection<String> {
        return myGradlePaths
    }

    override fun execute(controller: BuildController): PostBuildProjectModels? {
        val postBuildProjectModels = PostBuildProjectModels()

        if (!myGradlePaths.isEmpty()) {
            val rootProject = controller.buildModel.rootProject
            val root = controller.findModel(rootProject, GradleProject::class.java)
            postBuildProjectModels.populate(root, myGradlePaths, controller)
        }

        return postBuildProjectModels
    }

    class PostBuildProjectModels() : Serializable {
        // Key: module's Gradle path.
        private val myModelsByModule: MutableMap<String, PostBuildModuleModels> = HashMap()
        fun populate(rootProject: GradleProject,
                     gradleModulePaths: Collection<String>,
                     controller: BuildController) {
            for (gradleModulePath in gradleModulePaths) {
                populateModule(rootProject, gradleModulePath)
            }
        }

        private fun populateModule(rootProject: GradleProject,
                                   moduleProjectPath: String) {
            if (myModelsByModule.containsKey(moduleProjectPath)) {
                return
            }
            val moduleProject = rootProject.findByPath(moduleProjectPath)
            if (moduleProject != null) {
                val models = PostBuildModuleModels(moduleProject)
                models.populate()
                myModelsByModule[moduleProject.path] = models
            }
        }

        fun getModels(gradlePath: String): PostBuildModuleModels? {
            return myModelsByModule[gradlePath]
        }
    }

    class PostBuildModuleModels(private val myGradleProject: GradleProject) : Serializable {
        fun populate() {

        }
    }
}
