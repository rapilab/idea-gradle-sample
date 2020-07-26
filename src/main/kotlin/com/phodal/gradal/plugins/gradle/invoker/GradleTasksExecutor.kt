/*
 * Copyright (C) 2013 The Android Open Source Project
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
package com.phodal.gradal.plugins.gradle.invoker

import com.intellij.notification.NotificationGroup
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project

/**
 * Invokes Gradle tasks as a IDEA task in the background.
 */
abstract class GradleTasksExecutor protected constructor(project: Project?) : Backgroundable(project, "Gradal... Build Running", true) {
    /**
     * Regular [.queue] method might return immediately if current task is executed in a separate non-calling thread.
     *
     *
     * However, sometimes we want to wait for the task completion, e.g. consider a use-case when we execute an IDE run configuration.
     * It opens dedicated run/debug tool window and displays execution output there. However, it is shown as finished as soon as
     * control flow returns. That's why we don't want to return control flow until the actual task completion.
     *
     *
     * This method allows to achieve that target - it executes gradle tasks under the IDE 'progress management system' (shows progress
     * bar at the bottom) in a separate thread and doesn't return control flow to the calling thread until all target tasks are actually
     * executed.
     */
    abstract fun queueAndWaitForCompletion()

    companion object {
        val LOGGING_NOTIFICATION = NotificationGroup.logOnlyGroup("Gradal... Build (Logging)")
        val BALLOON_NOTIFICATION = NotificationGroup.balloonGroup("Gradal... Build (Balloon)")
    }
}
