package com.phodal.gradal.plugins.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.ModuleManager
import com.phodal.gradal.plugins.gradle.GradleTasksExecutorImpl
import com.phodal.gradal.plugins.gradle.run.OutputBuildActionUtil
import org.gradle.tooling.LongRunningOperation


class GetGradleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val projectPath = project.basePath!!
//
//        val toolWindow: ToolWindow = ToolWindowManager.getInstance(e.project!!).getToolWindow("MyPlugin")!!
//        val consoleView: ConsoleView = TextConsoleBuilderFactory.getInstance().createBuilder(e.project!!).console
//        val content: Content = toolWindow.contentManager.getFactory().createContent(consoleView.getComponent(), "MyPlugin Output", false)
//        toolWindow.contentManager.addContent(content)
//        consoleView.print("Hello from MyPlugin!", ConsoleViewContentType.NORMAL_OUTPUT)


        val moduleManager = ModuleManager.getInstance(project)
        val modules = moduleManager.modules
        val buildAction = OutputBuildActionUtil.create(modules);

        GradleTasksExecutorImpl().executeTask(project, projectPath, buildAction)
    }
}
