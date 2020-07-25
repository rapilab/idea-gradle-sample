package com.phodal.gradal.plugins.gradle.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GradleFacetType extends FacetType<GradleFacet, GradleFacetConfiguration> {
  public GradleFacetType() {
    super(GradleFacet.getFacetTypeId(), GradleFacet.getFacetId(), GradleFacet.getFacetName());
  }

  @NotNull
  @Override
  public GradleFacetConfiguration createDefaultConfiguration() {
    return new GradleFacetConfiguration();
  }

  @NotNull
  @Override
  public GradleFacet createFacet(@NotNull Module module,
                                 @NotNull String name,
                                 @NotNull GradleFacetConfiguration configuration,
                                 @Nullable Facet underlyingFacet) {
    return new GradleFacet(module, name, configuration);
  }

  @Override
  public boolean isSuitableModuleType(ModuleType moduleType) {
    return true;
  }
}

