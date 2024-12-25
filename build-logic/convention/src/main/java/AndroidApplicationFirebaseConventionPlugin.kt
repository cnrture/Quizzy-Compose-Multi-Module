package com.canerture.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                if (pluginManager.hasPlugin("com.android.application")) {
                    apply("com.google.gms.google-services")
                }
            }

            dependencies {
                val bom = libs.findLibrary("firebase").get()
                "implementation"(platform(bom))
                "implementation"(libs.findLibrary("firebase-auth").get())
            }
        }
    }
}
