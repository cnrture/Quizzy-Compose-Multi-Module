package com.canerture.convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<LibraryExtension>()

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
        }
    }
}
