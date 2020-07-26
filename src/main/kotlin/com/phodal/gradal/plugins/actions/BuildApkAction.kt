package com.phodal.gradal.plugins.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.module.ModuleManager
import com.phodal.gradal.plugins.gradle.GradleBuildInvoker
import com.phodal.gradal.plugins.gradle.run.OutputBuildActionUtil


class BuildApkAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val projectPath = project.basePath!!

        val moduleManager = ModuleManager.getInstance(project)
        val modules = moduleManager.modules
        val buildAction = OutputBuildActionUtil.create(modules);

        val invoker = GradleBuildInvoker.getInstance(project)
        invoker.assemble(projectPath, buildAction);
    }
}
