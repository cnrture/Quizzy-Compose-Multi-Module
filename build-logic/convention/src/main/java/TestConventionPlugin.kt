package com.canerture.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

private val coverageExclusions = listOf(
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*_Hilt*.class",
    "**/Hilt_*.class",
    "**/*_Factory.class",
    "**/*_MembersInjector.class",
    "**/*Module.class",
    "**/*Module_*Factory.class",
    "**/Dagger*Component*.class",
    "**/*Contract*.class",
    "**/*TestTags*.class",
    "**/*ComposableSingletons*.class",
    "**/*Preview*.class",
)

class TestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("truth").get())
                "testImplementation"(libs.findLibrary("turbine").get())
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
            }

            configureJacoco()
        }
    }

    private fun Project.configureJacoco() {
        pluginManager.apply("jacoco")

        extensions.configure(JacocoPluginExtension::class.java) {
            toolVersion = libs.findVersion("jacoco").get().requiredVersion
        }

        tasks.withType(Test::class.java).configureEach {
            extensions.configure(JacocoTaskExtension::class.java) {
                isIncludeNoLocationClasses = true
                excludes = listOf("jdk.internal.*")
            }
        }

        pluginManager.withPlugin("com.android.library") {
            extensions.configure(LibraryExtension::class.java) {
                buildTypes.getByName("debug") {
                    enableUnitTestCoverage = true
                }
            }
        }

        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            registerJvmJacocoReport()
        }
    }

    private fun Project.registerJvmJacocoReport() {
        tasks.register<JacocoReport>("jacocoTestReport") {
            group = "verification"
            description = "Generates a JaCoCo coverage report for this module's unit tests."
            dependsOn("test")

            reports {
                html.required.set(true)
                xml.required.set(true)
                csv.required.set(false)
            }

            classDirectories.setFrom(
                files(
                    fileTree(layout.buildDirectory.dir("classes/kotlin/main")) {
                        exclude(coverageExclusions)
                    },
                ),
            )
            sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
            executionData.setFrom(
                fileTree(layout.buildDirectory) { include("jacoco/test.exec") },
            )
            setOnlyIf { true }
        }
    }
}
