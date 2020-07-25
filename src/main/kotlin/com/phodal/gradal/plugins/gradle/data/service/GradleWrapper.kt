package com.phodal.gradal.plugins.gradle.data.service

import com.google.common.base.Strings
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import org.gradle.wrapper.WrapperExecutor
import org.jetbrains.annotations.NonNls
import java.io.File
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class GradleWrapper private constructor(private val myPropertiesFilePath: File, private val myProject: Project?) {
    @get:Throws(IOException::class)
    val properties: Properties
        get() = PropertiesFiles.getProperties(myPropertiesFilePath)

    @get:Throws(IOException::class)
    val gradleFullVersion: String?
        get() {
            val url = properties.getProperty(WrapperExecutor.DISTRIBUTION_URL_PROPERTY)
            if (url != null) {
                val m = GRADLE_DISTRIBUTION_URL_PATTERN.matcher(url)
                if (m.matches()) {
                    return m.group(1) + Strings.nullToEmpty(m.group(2))
                }
            }
            return null
        }

    companion object {
        const val FD_GRADLE = "gradle" //$NON-NLS-1$
        val FD_GRADLE_WRAPPER = FD_GRADLE + File.separator + "wrapper" //$NON-NLS-1$

        /** project local property file  */
        const val FN_GRADLE_WRAPPER_PROPERTIES = "gradle-wrapper.properties" //$NON-NLS-1$

        @NonNls
        val GRADLEW_PROPERTIES_PATH = FileUtil.join(FD_GRADLE_WRAPPER, FN_GRADLE_WRAPPER_PROPERTIES)
        private val GRADLE_DISTRIBUTION_URL_PATTERN = Pattern.compile(".*/gradle-([^-]+)(-[^\\/\\\\]+)?-(bin|all).zip")
        fun find(project: Project): GradleWrapper? {
            val basePath = project.basePath
                    ?: // Default project. Unlikely to happen.
                    return null
            val baseDir = File(basePath)
            val propertiesFilePath = getDefaultPropertiesFilePath(baseDir)
            return if (propertiesFilePath.isFile) GradleWrapper(propertiesFilePath, project) else null
        }

        fun getDefaultPropertiesFilePath(projectPath: File): File {
            return File(projectPath, GRADLEW_PROPERTIES_PATH)
        }
    }

}
