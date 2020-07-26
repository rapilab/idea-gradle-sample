package com.phodal.gradal.plugins.gradle.invoker

import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId
import com.intellij.openapi.progress.ProgressIndicator
import org.gradle.tooling.CancellationTokenSource
import org.jetbrains.annotations.TestOnly
import java.util.concurrent.ConcurrentHashMap

class BuildStopper {
    private val myMap: MutableMap<ExternalSystemTaskId, CancellationTokenSource> = ConcurrentHashMap()
    fun register(taskId: ExternalSystemTaskId, tokenSource: CancellationTokenSource) {
        myMap[taskId] = tokenSource
    }

    fun attemptToStopBuild(id: ExternalSystemTaskId, progressIndicator: ProgressIndicator?) {
        if (progressIndicator != null) {
            if (progressIndicator.isCanceled) {
                return
            }
            if (progressIndicator.isRunning) {
                progressIndicator.text = "Stopping Gradle build..."
                progressIndicator.cancel()
            }
        }
        val token = myMap[id]
        token?.cancel()
    }

    fun remove(taskId: ExternalSystemTaskId): CancellationTokenSource? {
        return myMap.remove(taskId)
    }

    operator fun contains(taskId: ExternalSystemTaskId): Boolean {
        return myMap.containsKey(taskId)
    }

    @TestOnly
    operator fun get(taskId: ExternalSystemTaskId): CancellationTokenSource? {
        return myMap[taskId]
    }
}
