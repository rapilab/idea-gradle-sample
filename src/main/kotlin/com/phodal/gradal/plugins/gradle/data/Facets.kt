package com.phodal.gradal.plugins.gradle.data

import com.intellij.facet.Facet
import com.intellij.facet.FacetTypeId
import com.intellij.facet.ModifiableFacetModel
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.module.Module
import org.jetbrains.annotations.Nullable

object Facets {
    @SafeVarargs
    fun <T : Facet<*>?> removeAllFacets(facetModel: ModifiableFacetModel, vararg typeIds: FacetTypeId<T>) {
        for (typeId in typeIds) {
            val facets = facetModel.getFacetsByType(typeId)
            for (facet in facets) {
                facetModel.removeFacet(facet)
            }
        }
    }

    fun <T : Facet<*>?> findFacet(module: Module,
                                  modelsProvider: IdeModifiableModelsProvider,
                                  typeId: FacetTypeId<T>): @Nullable T? {
        val facetModel = modelsProvider.getModifiableFacetModel(module)
        return facetModel.getFacetByType(typeId)
    }
}
