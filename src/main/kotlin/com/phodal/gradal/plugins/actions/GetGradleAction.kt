package com.phodal.gradal.plugins.actions

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.phodal.gradal.plugins.gradle.GradleTasksExecutorImpl


class GetGradleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val projectPath = project.basePath!!

        val toolWindow: ToolWindow = ToolWindowManager.getInstance(e.project!!).getToolWindow("MyPlugin")!!
        val consoleView: ConsoleView = TextConsoleBuilderFactory.getInstance().createBuilder(e.project!!).console
        val content: Content = toolWindow.contentManager.getFactory().createContent(consoleView.getComponent(), "MyPlugin Output", false)
        toolWindow.contentManager.addContent(content)
        consoleView.print("Hello from MyPlugin!", ConsoleViewContentType.NORMAL_OUTPUT)

        GradleTasksExecutorImpl().executeTask(project, projectPath)
    }
}
