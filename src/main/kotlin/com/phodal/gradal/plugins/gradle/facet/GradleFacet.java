package com.phodal.gradal.plugins.gradle.facet;

import com.intellij.facet.*;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GradleFacet extends Facet<GradleFacetConfiguration> {
  public static final String ANDROID_GRADLE_FACET_ID = "android-gradle";
  public static final String ANDROID_GRADLE_FACET_NAME = "Android-Gradle";

  @NotNull
  private static final FacetTypeId<GradleFacet> TYPE_ID = new FacetTypeId<>("android-gradle");

  @Nullable
  public static GradleFacet getInstance(@NotNull Module module) {
    return FacetManager.getInstance(module).getFacetByType(getFacetTypeId());
  }

  public GradleFacet(@NotNull Module module,
                     @NotNull String name,
                     @NotNull GradleFacetConfiguration configuration) {
    super(getFacetType(), module, name, configuration, null);
  }

  @NotNull
  public static String getFacetId() {
    return ANDROID_GRADLE_FACET_ID;
  }


  @NotNull
  public static String getFacetName() {
    return ANDROID_GRADLE_FACET_NAME;
  }

  @NotNull
  public static GradleFacetType getFacetType() {
    FacetType facetType = FacetTypeRegistry.getInstance().findFacetType(getFacetId());
    assert facetType instanceof GradleFacetType;
    return (GradleFacetType)facetType;
  }

  @NotNull
  public static FacetTypeId<GradleFacet> getFacetTypeId() {
    return TYPE_ID;
  }
}
