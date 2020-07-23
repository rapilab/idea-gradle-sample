package com.phodal.gradal.plugins.services

import com.intellij.openapi.project.Project
import com.phodal.gradal.plugins.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
