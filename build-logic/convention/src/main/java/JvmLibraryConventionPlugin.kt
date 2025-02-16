package com.canerture.convention

import com.canerture.convention.com.canerture.config.AppConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("quiz.detekt")
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = AppConfig.JAVA_VERSION
                targetCompatibility = AppConfig.JAVA_VERSION
            }

            extensions.configure<KotlinJvmProjectExtension> {
                compilerOptions.apply {
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }
        }
    }
}