package com.phodal.gradal.plugins.gradle.data;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetTypeId;
import com.intellij.facet.ModifiableFacetModel;
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class Facets {
  private Facets() {
  }

  @SafeVarargs
  public static <T extends Facet> void removeAllFacets(@NotNull ModifiableFacetModel facetModel, @NotNull FacetTypeId<T>... typeIds) {
    for (FacetTypeId<T> typeId : typeIds) {
      Collection<T> facets = facetModel.getFacetsByType(typeId);
      for (T facet : facets) {
        facetModel.removeFacet(facet);
      }
    }
  }

  @Nullable
  public static <T extends Facet> T findFacet(@NotNull Module module,
                                              @NotNull IdeModifiableModelsProvider modelsProvider,
                                              @NotNull FacetTypeId<T> typeId) {
    ModifiableFacetModel facetModel = modelsProvider.getModifiableFacetModel(module);
    return facetModel.getFacetByType(typeId);
  }
}

