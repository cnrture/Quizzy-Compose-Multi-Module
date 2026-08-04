package com.canerture.convention

import com.android.build.api.dsl.LibraryExtension
import com.canerture.convention.com.canerture.config.AppConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("quiz.detekt")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = AppConfig.COMPILE_SDK

                defaultConfig {
                    minSdk = AppConfig.MIN_SDK
                }

                compileOptions {
                    sourceCompatibility = AppConfig.JAVA_VERSION
                    targetCompatibility = AppConfig.JAVA_VERSION
                }

                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }
}
