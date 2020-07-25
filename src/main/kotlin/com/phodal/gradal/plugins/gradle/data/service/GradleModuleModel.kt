/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phodal.gradal.plugins.gradle.data.service

import com.google.common.annotations.VisibleForTesting
import com.google.common.collect.ImmutableList
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.serialization.PropertyMapping
import org.gradle.tooling.model.GradleProject
import org.jetbrains.kotlin.kapt.idea.KaptGradleModel
import java.io.File
import java.util.*

/**
 * Module-level Gradle information.
 */
class GradleModuleModel
/**
 * Method of constructing a GradleModuleModel without a GradleProject for use in tests ONLY.
 */ @VisibleForTesting @PropertyMapping("myModuleName", "myTaskNames", "myGradlePath", "myRootFolderPath", "myGradlePlugins", "myBuildFilePath", "myGradleVersion", "myAgpVersion", "myIsKaptEnabled") constructor(override val moduleName: String,
                                                                                                                                                                                                                   val taskNames: List<String?>,
                                                                                                                                                                                                                   val gradlePath: String,
                                                                                                                                                                                                                   val rootFolderPath: File,
                                                                                                                                                                                                                   val gradlePlugins: List<String>,
                                                                                                                                                                                                                   val buildFilePath: File?,
                                                                                                                                                                                                                   val gradleVersion: String?,
                                                                                                                                                                                                                   val agpVersion: String?,
                                                                                                                                                                                                                   val isKaptEnabled: Boolean) : ModuleModel {
    /**
     * @return the path of the Gradle project.
     */

    /**
     * @param moduleName    the name of the IDE module.
     * @param gradleProject the model obtained from Gradle.
     * @param gradlePlugins the list of gradle plugins applied to this module.
     * @param buildFilePath the path of the build.gradle file.
     * @param gradleVersion the version of Gradle used to sync the project.
     * @param agpVersion    the version of AGP used to sync the project.
     */
    constructor(moduleName: String,
                gradleProject: GradleProject,
                gradlePlugins: Collection<String>,
                buildFilePath: File?,
                gradleVersion: String?,
                agpVersion: String?,
                kaptGradleModel: KaptGradleModel?) : this(moduleName, getTaskNames(gradleProject), gradleProject.path,
            gradleProject.projectIdentifier.buildIdentifier.rootDir, ImmutableList.copyOf<String>(gradlePlugins), buildFilePath,
            gradleVersion, agpVersion, kaptGradleModel != null && kaptGradleModel.isEnabled) {
    }

    val buildFile: VirtualFile?
        get() = if (buildFilePath != null) VfsUtil.findFileByIoFile(buildFilePath, true) else null

    companion object {
        // Increase the value when adding/removing fields or when changing the serialization/deserialization mechanism.
        private const val serialVersionUID = 4L
        private const val GRADLE_PATH_SEPARATOR = ":" //$NON-NLS-1$
        private fun getTaskNames(gradleProject: GradleProject): List<String?> {
            val taskNames: MutableList<String?> = ArrayList()
            val tasks = gradleProject.tasks
            if (!tasks.isEmpty()) {
                for (task in tasks) {
                    val name = task.name
                    if (StringUtil.isNotEmpty(name)) {
                        taskNames.add(task.project.path + GRADLE_PATH_SEPARATOR + task.name)
                    }
                }
            }
            return taskNames
        }
    }

}
