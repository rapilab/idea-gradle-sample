# Idea Gradle Sample for testing


## notes

build logs

```
Executing tasks: [:app:generateDebugSources, :app:generateDebugAndroidTestSources, :app:createMockableJar, :app:compileDebugAndroidTestSources, :app:compileDebugUnitTestSources, :app:compileDebugSources] in project /Users/fdhuang/IntelliJIDEAProjects/MyApplication

> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:generateDebugBuildConfig UP-TO-DATE
> Task :app:prepareLintJar UP-TO-DATE
> Task :app:preDebugAndroidTestBuild SKIPPED
> Task :app:prepareLintJarForPublish
> Task :app:compileDebugAidl NO-SOURCE
> Task :app:compileDebugRenderscript NO-SOURCE
> Task :app:generateDebugSources
> Task :app:compileDebugAndroidTestAidl NO-SOURCE
> Task :app:extractDeepLinksDebug UP-TO-DATE
> Task :app:mainApkListPersistenceDebug UP-TO-DATE
> Task :app:createDebugCompatibleScreenManifests UP-TO-DATE
> Task :app:processDebugManifest
> Task :app:processDebugAndroidTestManifest
> Task :app:compileDebugAndroidTestRenderscript NO-SOURCE
> Task :app:generateDebugAndroidTestBuildConfig
> Task :app:generateDebugAndroidTestSources
> Task :app:createMockableJar UP-TO-DATE
> Task :app:generateDebugResValues UP-TO-DATE
> Task :app:generateDebugResources UP-TO-DATE
> Task :app:mainApkListPersistenceDebugAndroidTest
> Task :app:generateDebugAndroidTestResValues
> Task :app:generateDebugAndroidTestResources
> Task :app:preDebugUnitTestBuild UP-TO-DATE
> Task :app:processDebugJavaRes NO-SOURCE
> Task :app:processDebugUnitTestJavaRes NO-SOURCE
> Task :app:mergeDebugResources UP-TO-DATE
> Task :app:processDebugResources
> Task :app:compileDebugKotlin
> Task :app:mergeDebugAndroidTestResources
> Task :app:javaPreCompileDebug
> Task :app:compileDebugJavaWithJavac UP-TO-DATE
> Task :app:compileDebugUnitTestKotlin
> Task :app:processDebugAndroidTestResources
> Task :app:compileDebugSources UP-TO-DATE
> Task :app:javaPreCompileDebugUnitTest
> Task :app:compileDebugUnitTestJavaWithJavac NO-SOURCE
> Task :app:compileDebugUnitTestSources UP-TO-DATE
> Task :app:bundleDebugClasses
> Task :app:compileDebugAndroidTestKotlin
> Task :app:javaPreCompileDebugAndroidTest

> Task :app:compileDebugAndroidTestJavaWithJavac
warning: [options] source value 7 is obsolete and will be removed in a future release
warning: [options] target value 7 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
3 warnings

> Task :app:compileDebugAndroidTestSources

BUILD SUCCESSFUL in 8s
25 actionable tasks: 17 executed, 8 up-to-date
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
