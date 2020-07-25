/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.phodal.gradal.plugins.gradle.facet

import com.intellij.facet.FacetConfiguration
import com.intellij.facet.ui.FacetEditorContext
import com.intellij.facet.ui.FacetEditorTab
import com.intellij.facet.ui.FacetValidatorsManager
import com.intellij.openapi.util.InvalidDataException
import com.intellij.openapi.util.WriteExternalException
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import org.jetbrains.annotations.NonNls

/**
 * Configuration options for the Android-Gradle facet. In Android Studio, these options *cannot* be directly changed by users.
 */
class GradleFacetConfiguration : FacetConfiguration {
    @NonNls
    var GRADLE_PROJECT_PATH: String? = null

    @NonNls
    var LAST_SUCCESSFUL_SYNC_AGP_VERSION: String? = null

    @NonNls
    var LAST_KNOWN_AGP_VERSION: String? = null
    override fun createEditorTabs(editorContext: FacetEditorContext,
                                  validatorsManager: FacetValidatorsManager): Array<FacetEditorTab?> {
        return arrayOfNulls(0)
    }

    @Throws(InvalidDataException::class)
    override fun readExternal(element: Element) {
        XmlSerializer.deserializeInto(this, element)
    }

    @Throws(WriteExternalException::class)
    override fun writeExternal(element: Element) {
        XmlSerializer.serializeInto(this, element)
    }
}
