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
package com.phodal.gradal.plugins.gradle.importing

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.java.LanguageLevel
import com.intellij.util.ExceptionUtil
import org.jetbrains.annotations.NonNls
import java.io.File
import java.io.IOException
import com.phodal.gradal.plugins.gradle.importing.GradleProjectImporter as GradleProjectImporter1

/**
 * Imports an Android-Gradle project without showing the "Import Project" Wizard UI.
 */
class GradleProjectImporter {
    fun importProjectCore(projectFolder: VirtualFile) {
        var newProject: Project?
        val projectFolderPath = VfsUtilCore.virtualToIoFile(projectFolder)
        try {
            setUpLocalProperties(projectFolderPath)
            val projectName = projectFolder.name
            newProject = createProject(projectName, projectFolderPath)
            importProjectNoSync(Request(newProject))
        } catch (e: Throwable) {
            if (ApplicationManager.getApplication().isUnitTestMode) {
                ExceptionUtil.rethrowUnchecked(e)
            }
            Messages.showErrorDialog(e.message, "Project Import")
            logger.error(e)
            newProject = null
        }
    }

    @Throws(IOException::class)
    private fun setUpLocalProperties(projectFolderPath: File) {
    }

    private val logger: Logger
        private get() = Logger.getInstance(javaClass)

    @Throws(IOException::class)
    fun importProjectNoSync(request: Request) {
    }

    fun createProject(projectName: String, projectFolderPath: File): Project {
        val projectManager = ProjectManager.getInstance()
        val newProject = projectManager.createProject(projectName, projectFolderPath.toString())
        //    newProject = myNewProjectSetup.createProject(projectName, projectFolderPath.getPath());
        return newProject!!
    }

    class Request(val project: Project) {
        var javaLanguageLevel: LanguageLevel? = null
        var isNewProject = false

    }

    companion object {
        // A copy of a private constant from GradleJvmStartupActivity.
        @NonNls
        private val SHOW_UNLINKED_GRADLE_POPUP = "show.inlinked.gradle.project.popup"
        fun getInstance() = ServiceManager.getService(com.phodal.gradal.plugins.gradle.importing.GradleProjectImporter::class.java)!!
    }
}
