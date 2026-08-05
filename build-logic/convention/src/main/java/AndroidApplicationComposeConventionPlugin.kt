package com.canerture.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<ApplicationExtension>()

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                if (project.providers.gradleProperty("quizzy.enableComposeCompilerReports").orNull == "true") {
                    val reportDir = rootProject.layout.buildDirectory
                        .dir("compose_reports/${project.path.replace(":", "-").trim('-')}")
                    metricsDestination.set(reportDir)
                    reportsDestination.set(reportDir)
                }
            }

            with(extension) {
                buildFeatures {
                    compose = true
                }

                dependencies {
                    val bom = libs.findLibrary("compose-bom").get()
                    "implementation"(platform(bom))
                    "implementation"(libs.findLibrary("material3").get())
                    "androidTestImplementation"(platform(bom))
                    "implementation"(libs.findLibrary("compose-ui-tooling-preview").get())
                    "debugImplementation"(libs.findLibrary("compose-ui-tooling").get())
                }
            }
        }
    }
}