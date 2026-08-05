// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.plugin) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlinx.serialization.plugin) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.detekt.plugin) apply false
    alias(libs.plugins.module.graph) apply true
}

tasks.register("printModulePaths") {
    description = "Prints the paths of all modules in the project."
    subprojects {
        if (subprojects.isEmpty()) {
            println(this.path)
        }
    }
}

// Unit-test coverage is per-module and driven by AGP's built-in unit-test coverage
// (enabled in the `quiz.test` convention plugin). Run coverage for the whole project with:
//
//   ./gradlew createDebugUnitTestCoverageReport
//
// Gradle fans that task name out to every module that has it; each writes its own report to
// build/reports/coverage/test/debug/index.html. A hand-rolled aggregate report is intentionally
// not used here: AGP runs unit tests against ASM-transformed classes, so a cross-module JacocoReport
// pointed at plain Kotlin output hits a class-id mismatch and reports 0% (see TestConventionPlugin).