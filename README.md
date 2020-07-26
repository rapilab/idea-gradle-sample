# Gardal: Idea Gradle Sample for testing


## Process

start: BuildApkAction

1. prepare paths:  -> OutputBuildActionUtil -> OutputBuildAction -> BulidPath -> ProjectPaths
2. prepare task & listener: assemble -> createRequest for Task Background -> createTask Listener for UI
3. run tasks: GradleTasksExecutorImpl -> run -> invokeGradleTasks -> GradleExecutionHelper.execute

## notes

Gradal Build Logs:

```bash
Executing tasks: [assemble] in project /Users/fdhuang/IntelliJIDEAProjects/MyApplication

Starting Gradle Daemon...
Gradle Daemon started in 892 ms
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:compileDebugAidl NO-SOURCE
> Task :app:generateDebugBuildConfig UP-TO-DATE
> Task :app:compileDebugRenderscript NO-SOURCE
> Task :app:mainApkListPersistenceDebug UP-TO-DATE
> Task :app:generateDebugResValues UP-TO-DATE
> Task :app:generateDebugResources UP-TO-DATE
> Task :app:mergeDebugResources UP-TO-DATE
> Task :app:createDebugCompatibleScreenManifests UP-TO-DATE
> Task :app:extractDeepLinksDebug UP-TO-DATE
> Task :app:mergeDebugShaders UP-TO-DATE
> Task :app:processDebugManifest UP-TO-DATE
> Task :app:processDebugResources UP-TO-DATE
> Task :app:compileDebugKotlin UP-TO-DATE
> Task :app:javaPreCompileDebug UP-TO-DATE
> Task :app:compileDebugJavaWithJavac UP-TO-DATE
> Task :app:compileDebugSources UP-TO-DATE
> Task :app:compileDebugShaders UP-TO-DATE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets UP-TO-DATE
> Task :app:processDebugJavaRes NO-SOURCE
> Task :app:mergeDebugJavaResource UP-TO-DATE
> Task :app:dexBuilderDebug UP-TO-DATE
> Task :app:checkDebugDuplicateClasses UP-TO-DATE
> Task :app:mergeDebugJniLibFolders UP-TO-DATE
> Task :app:validateSigningDebug UP-TO-DATE
> Task :app:mergeDebugNativeLibs UP-TO-DATE
> Task :app:mergeExtDexDebug UP-TO-DATE
> Task :app:mergeDexDebug UP-TO-DATE
> Task :app:stripDebugDebugSymbols UP-TO-DATE
> Task :app:packageDebug UP-TO-DATE
> Task :app:assembleDebug UP-TO-DATE
> Task :app:preReleaseBuild UP-TO-DATE
> Task :app:compileReleaseAidl NO-SOURCE
> Task :app:compileReleaseRenderscript NO-SOURCE
> Task :app:generateReleaseBuildConfig UP-TO-DATE
> Task :app:mainApkListPersistenceRelease UP-TO-DATE
> Task :app:generateReleaseResValues UP-TO-DATE
> Task :app:generateReleaseResources UP-TO-DATE
> Task :app:mergeReleaseResources UP-TO-DATE
> Task :app:createReleaseCompatibleScreenManifests UP-TO-DATE
> Task :app:extractDeepLinksRelease UP-TO-DATE
> Task :app:processReleaseManifest UP-TO-DATE
> Task :app:processReleaseResources UP-TO-DATE
> Task :app:compileReleaseKotlin UP-TO-DATE
> Task :app:javaPreCompileRelease UP-TO-DATE
> Task :app:compileReleaseJavaWithJavac UP-TO-DATE
> Task :app:compileReleaseSources UP-TO-DATE
> Task :app:prepareLintJar UP-TO-DATE
> Task :app:checkReleaseDuplicateClasses UP-TO-DATE
> Task :app:lintVitalRelease
> Task :app:dexBuilderRelease UP-TO-DATE
> Task :app:mergeReleaseShaders UP-TO-DATE
> Task :app:compileReleaseShaders UP-TO-DATE
> Task :app:generateReleaseAssets UP-TO-DATE
> Task :app:mergeReleaseAssets UP-TO-DATE
> Task :app:mergeExtDexRelease UP-TO-DATE
> Task :app:mergeDexRelease UP-TO-DATE
> Task :app:processReleaseJavaRes NO-SOURCE
> Task :app:mergeReleaseJavaResource UP-TO-DATE
> Task :app:mergeReleaseJniLibFolders UP-TO-DATE
> Task :app:mergeReleaseNativeLibs UP-TO-DATE
> Task :app:stripReleaseDebugSymbols UP-TO-DATE
> Task :app:packageRelease UP-TO-DATE
> Task :app:assembleRelease
> Task :app:assemble

BUILD SUCCESSFUL in 21s
49 actionable tasks: 1 executed, 48 up-to-date
```

Android Studio logs:

```
Executing tasks: [:app:assembleDebug] in project /Users/fdhuang/works/fework/MyApplication

Starting Gradle Daemon...
Gradle Daemon started in 868 ms
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:generateDebugBuildConfig UP-TO-DATE
> Task :app:compileDebugAidl NO-SOURCE
> Task :app:compileDebugRenderscript NO-SOURCE
> Task :app:generateDebugResValues UP-TO-DATE
> Task :app:generateDebugResources UP-TO-DATE
> Task :app:createDebugCompatibleScreenManifests UP-TO-DATE
> Task :app:extractDeepLinksDebug UP-TO-DATE
> Task :app:processDebugManifest UP-TO-DATE
> Task :app:mergeDebugResources UP-TO-DATE
> Task :app:processDebugResources UP-TO-DATE
> Task :app:compileDebugKotlin UP-TO-DATE
> Task :app:javaPreCompileDebug UP-TO-DATE
> Task :app:compileDebugJavaWithJavac UP-TO-DATE
> Task :app:compileDebugSources UP-TO-DATE
> Task :app:mergeDebugShaders UP-TO-DATE
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets UP-TO-DATE
> Task :app:processDebugJavaRes NO-SOURCE
> Task :app:dexBuilderDebug UP-TO-DATE
> Task :app:mergeDebugJavaResource UP-TO-DATE
> Task :app:checkDebugDuplicateClasses UP-TO-DATE
> Task :app:mergeExtDexDebug UP-TO-DATE
> Task :app:mergeDexDebug UP-TO-DATE
> Task :app:mergeDebugJniLibFolders UP-TO-DATE
> Task :app:mergeDebugNativeLibs UP-TO-DATE
> Task :app:stripDebugDebugSymbols NO-SOURCE
> Task :app:validateSigningDebug UP-TO-DATE
> Task :app:packageDebug UP-TO-DATE
> Task :app:assembleDebug UP-TO-DATE

BUILD SUCCESSFUL in 10s
21 actionable tasks: 21 up-to-date

Build Analyzer results available
```
