package com.phodal.gradal.plugins.gradle.facet

import com.intellij.ProjectTopics
import com.intellij.facet.Facet
import com.intellij.facet.FacetManager
import com.intellij.facet.FacetTypeId
import com.intellij.facet.FacetTypeRegistry
import com.intellij.facet.impl.FacetUtil.saveFacetConfiguration
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootEvent
import com.intellij.openapi.roots.ModuleRootListener
import com.intellij.openapi.util.WriteExternalException

class GradleFacet(module: Module,
                  name: String,
                  configuration: GradleFacetConfiguration) : Facet<GradleFacetConfiguration?>(facetType, module, name, configuration, null) {

    override fun initFacet() {
        val connection = module.messageBus.connect(this)
        connection.subscribe(ProjectTopics.PROJECT_ROOTS, object : ModuleRootListener {
            override fun rootsChanged(event: ModuleRootEvent) {
                ApplicationManager.getApplication().invokeLater {
                    if (!isDisposed) {
                        val project = module.project
                        WriteCommandAction.runWriteCommandAction(project) { updateConfiguration() }
                    }
                }
            }
        })
        updateConfiguration()
    }

    private fun updateConfiguration() {
        val config = configuration
        try {
            saveFacetConfiguration(config)
        } catch (e: WriteExternalException) {
            Logger.getInstance(GradleFacet::class.java).error("Unable to save contents of 'Android-Gradle' facet", e)
        }
    }

    companion object {
        const val facetId = "android-gradle"
        const val facetName = "Android-Gradle"
        val TYPE_ID = FacetTypeId<GradleFacet>(facetId)

        fun getInstance(module: Module): GradleFacet? {
            return FacetManager.getInstance(module).getFacetByType(getFacetTypeId())
        }

        fun getFacetTypeId(): FacetTypeId<GradleFacet> {
            return TYPE_ID
        }

        val facetType: GradleFacetType
            get() {
                val facetType = FacetTypeRegistry.getInstance().findFacetType(facetId)
                assert(facetType is GradleFacetType)
                return facetType as GradleFacetType
            }

    }
}
