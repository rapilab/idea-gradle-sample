package com.phodal.gradal.plugins.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.phodal.gradal.plugins.gradle.GradleTasksExecutorImpl
import com.phodal.gradal.plugins.gradle.facet.GradleFacet
import com.phodal.gradal.plugins.gradle.run.OutputBuildActionUtil
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.stream.Collectors


class BuildApkAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val projectPath = project.basePath!!

//        val toolWindow: ToolWindow = ToolWindowManager.getInstance(e.project!!).getToolWindow("MyPlugin")!!
//        val consoleView: ConsoleView = TextConsoleBuilderFactory.getInstance().createBuilder(e.project!!).console
//        val content: Content = toolWindow.contentManager.getFactory().createContent(consoleView.getComponent(), "MyPlugin Output", false)
//        toolWindow.contentManager.addContent(content)
//        consoleView.print("Hello from MyPlugin!", ConsoleViewContentType.NORMAL_OUTPUT)

        val appModules = analyzeProjectStructure(project)
                .stream()
                .collect(Collectors.toList());

        val moduleManager = ModuleManager.getInstance(project)
        val modules = moduleManager.modules
        val buildAction = OutputBuildActionUtil.create(modules);

        GradleTasksExecutorImpl().executeTask(project, projectPath, buildAction)
    }

    private fun analyzeProjectStructure(myProject: Project): Queue<Module> {
        val appModules: Queue<Module> = ConcurrentLinkedQueue()

        val moduleManager = ModuleManager.getInstance(myProject)
        val modules = moduleManager.modules

        for (module in modules) {
            val gradleFacet: GradleFacet? = GradleFacet.getInstance(module)
            if (gradleFacet != null) {
                val gradlePath = gradleFacet.configuration.GRADLE_PROJECT_PATH
                appModules.add(module)
            }
        }

        return appModules
    }
}
