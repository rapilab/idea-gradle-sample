package com.phodal.gradal.plugins.gradle.run;

import com.google.common.collect.ImmutableSet;
import org.gradle.tooling.BuildAction;
import org.gradle.tooling.BuildController;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.gradle.BasicGradleProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OutputBuildAction implements BuildAction<OutputBuildAction.PostBuildProjectModels>, Serializable {
    @NotNull private final ImmutableSet<String> myGradlePaths;

    OutputBuildAction(@NotNull Collection<String> moduleGradlePaths) {
        myGradlePaths = ImmutableSet.copyOf(moduleGradlePaths);
    }
    @TestOnly
    @NotNull
    Collection<String> getMyGradlePaths() {
        return myGradlePaths;
    }

    @Override
    public PostBuildProjectModels execute(BuildController controller) {
        return null;
    }

    public static class PostBuildProjectModels implements Serializable {
        // Key: module's Gradle path.
        @NotNull private final Map<String, PostBuildModuleModels> myModelsByModule = new HashMap<>();

        private PostBuildProjectModels() {}

        public void populate(@NotNull GradleProject rootProject,
                             @NotNull Collection<String> gradleModulePaths,
                             @NotNull BuildController controller) {
            for (String gradleModulePath : gradleModulePaths) {
                populateModule(rootProject, gradleModulePath, controller);
            }
        }

        private void populateModule(@NotNull GradleProject rootProject,
                                    @NotNull String moduleProjectPath,
                                    @NotNull BuildController controller) {
            if (myModelsByModule.containsKey(moduleProjectPath)) {
                // Module models already found
                return;
            }

            GradleProject moduleProject = rootProject.findByPath(moduleProjectPath);
            if (moduleProject != null) {
                PostBuildModuleModels models = new PostBuildModuleModels(moduleProject);
                models.populate(controller);
                myModelsByModule.put(moduleProject.getPath(), models);
            }
        }

        @Nullable
        public PostBuildModuleModels getModels(@NotNull String gradlePath) {
            return myModelsByModule.get(gradlePath);
        }
    }

    public static class PostBuildModuleModels implements Serializable {
        @NotNull private final GradleProject myGradleProject;
        @NotNull private final Map<Class, Object> myModelsByType = new HashMap<>();

        private PostBuildModuleModels(@NotNull GradleProject gradleProject) {
            myGradleProject = gradleProject;
        }

        private void populate(@NotNull BuildController controller) {

        }

        @NotNull
        public String getGradlePath() {
            return myGradleProject.getPath();
        }

        @NotNull
        public String getModuleName() {
            return myGradleProject.getName();
        }

        @Nullable
        private <T> T findAndAddModel(@NotNull BuildController controller, @NotNull Class<T> modelType) {
            T model = controller.findModel(myGradleProject, modelType);
            if (model != null) {
                myModelsByType.put(modelType, model);
            }
            return model;
        }

        public <T> boolean hasModel(@NotNull Class<T> modelType) {
            return findModel(modelType) != null;
        }

        @Nullable
        public <T> T findModel(@NotNull Class<T> modelType) {
            Object model = myModelsByType.get(modelType);
            if (model != null) {
                assert modelType.isInstance(model);
                return modelType.cast(model);
            }
            return null;
        }
    }
}
