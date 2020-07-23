package com.phodal.gradal.plugins.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.phodal.gradal.plugins.gradle.GradleTasksExecutorImpl

class GetGradleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val projectPath = project.basePath!!

        GradleTasksExecutorImpl().executeTask(project, projectPath)
    }
}
