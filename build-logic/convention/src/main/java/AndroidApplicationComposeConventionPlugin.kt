package com.canerture.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<ApplicationExtension>()

            with(extension) {
                buildFeatures {
                    compose = true
                }

                dependencies {
                    val bom = libs.findLibrary("compose-bom").get()
                    "implementation"(platform(bom))
                    "androidTestImplementation"(platform(bom))
                    "implementation"(libs.findLibrary("compose-ui-tooling-preview").get())
                    "debugImplementation"(libs.findLibrary("compose-ui-tooling").get())
                }
            }

            /*extensions.configure<ComposeCompilerGradlePluginExtension> {
                fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
                fun Provider<*>.relativeToRootProject(dir: String) = map {
                    isolated.rootProject.projectDirectory
                        .dir("build")
                        .dir(projectDir.toRelativeString(rootDir))
                }.map { it.dir(dir) }

                project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
                    .relativeToRootProject("compose-metrics")
                    .let(metricsDestination::set)

                project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
                    .relativeToRootProject("compose-reports")
                    .let(reportsDestination::set)

                stabilityConfigurationFile =
                    isolated.rootProject.projectDirectory.file("compose_compiler_config.conf")
            }*/
        }
    }
}