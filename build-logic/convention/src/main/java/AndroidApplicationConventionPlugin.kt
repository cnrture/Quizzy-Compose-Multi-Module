package com.canerture.convention

import com.android.build.api.dsl.ApplicationExtension
import com.canerture.convention.com.canerture.config.AppConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("quiz.detekt")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = AppConfig.COMPILE_SDK

                defaultConfig {
                    minSdk = AppConfig.MIN_SDK
                }

                compileOptions {
                    sourceCompatibility = AppConfig.JAVA_VERSION
                    targetCompatibility = AppConfig.JAVA_VERSION
                    isCoreLibraryDesugaringEnabled = true
                }
                defaultConfig.targetSdk = AppConfig.TARGET_SDK

            }
            dependencies {
                add("coreLibraryDesugaring", libs.findLibrary("desugaring").get())
                add("implementation", libs.findLibrary("androidx-multidex").get())
            }
        }
    }
}
