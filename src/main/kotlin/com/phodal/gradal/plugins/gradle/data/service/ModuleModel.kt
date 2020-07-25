package com.phodal.gradal.plugins.gradle.data.service

import java.io.Serializable

interface ModuleModel : Serializable {
    val moduleName: String
}
