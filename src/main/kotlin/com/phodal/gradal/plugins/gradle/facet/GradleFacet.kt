package com.phodal.gradal.plugins.gradle.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetManager
import com.intellij.facet.FacetTypeId
import com.intellij.facet.FacetTypeRegistry
import com.intellij.openapi.module.Module

class GradleFacet(module: Module,
                  name: String,
                  configuration: GradleFacetConfiguration) : Facet<GradleFacetConfiguration?>(facetType, module, name, configuration, null) {
    companion object {
        const val facetId = "android-gradle"
        const val facetName = "Android-Gradle"
        val facetTypeId = FacetTypeId<GradleFacet>("android-gradle")
        fun getInstance(module: Module): GradleFacet? {
            return FacetManager.getInstance(module).getFacetByType(facetTypeId)
        }

        val facetType: GradleFacetType
            get() {
                val facetType = FacetTypeRegistry.getInstance().findFacetType(facetId)
                assert(facetType is GradleFacetType)
                return facetType as GradleFacetType
            }

    }
}
