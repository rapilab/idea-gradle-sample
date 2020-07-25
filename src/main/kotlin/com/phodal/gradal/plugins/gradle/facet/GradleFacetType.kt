package com.phodal.gradal.plugins.gradle.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetType
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType

class GradleFacetType : FacetType<GradleFacet, GradleFacetConfiguration>(GradleFacet.facetTypeId, GradleFacet.facetId, GradleFacet.facetName) {
    override fun createDefaultConfiguration(): GradleFacetConfiguration {
        return GradleFacetConfiguration()
    }

    override fun createFacet(module: Module,
                             name: String,
                             configuration: GradleFacetConfiguration,
                             underlyingFacet: Facet<*>?): GradleFacet {
        return GradleFacet(module, name, configuration)
    }

    override fun isSuitableModuleType(moduleType: ModuleType<*>?): Boolean {
        return true
    }
}
