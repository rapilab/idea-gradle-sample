package com.phodal.gradal.plugins.gradle.variant.view

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class BuildVariantToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        BuildVariantView.Companion.getInstance(project).createToolWindowContent(toolWindow)
    }

    companion object {
        const val ID = "Build Variants"
    }
}
