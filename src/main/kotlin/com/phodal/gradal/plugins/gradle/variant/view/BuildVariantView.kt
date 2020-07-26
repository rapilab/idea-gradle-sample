package com.phodal.gradal.plugins.gradle.variant.view

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.content.ContentFactory
import javax.swing.JComponent

class BuildVariantView {
    private val myToolWindowPanel: JComponent? = null
    fun createToolWindowContent(toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(myToolWindowPanel, "", false)
        toolWindow.contentManager.addContent(content)
        updateContents()
    }

    private fun updateContents() {}

    companion object {
        fun getInstance(project: Project): BuildVariantView {
            return ServiceManager.getService(project, BuildVariantView::class.java)
        }
    }
}
