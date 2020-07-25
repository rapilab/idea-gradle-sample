/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phodal.gradal.plugins.gradle.data.service

import java.io.Serializable

interface GradleModuleModels : Serializable {
    /**
     * Obtain the single model with given modelType.
     * Use [.findModels] if there're multiple models with given type.
     */
    fun <T> findModel(modelType: Class<T>): T?

    /**
     * Obtain list of models with the given modelType.
     */
    fun <T> findModels(modelType: Class<T>): List<T>?
    val moduleName: String
}
