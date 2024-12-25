import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.plugin)
    compileOnly(libs.compose.plugin)
    compileOnly(libs.kotlin.plugin)
    compileOnly(libs.ksp.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = libs.plugins.quiz.android.application.compose.get().pluginId
            implementationClass = "com.canerture.convention.AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.quiz.android.application.asProvider().get().pluginId
            implementationClass = "com.canerture.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.quiz.android.library.compose.get().pluginId
            implementationClass = "com.canerture.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.quiz.android.library.asProvider().get().pluginId
            implementationClass = "com.canerture.convention.AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.quiz.android.feature.get().pluginId
            implementationClass = "com.canerture.convention.AndroidFeatureConventionPlugin"
        }
        register("hilt") {
            id = libs.plugins.quiz.hilt.get().pluginId
            implementationClass = "com.canerture.convention.HiltConventionPlugin"
        }
        register("androidFirebase") {
            id = libs.plugins.quiz.android.firebase.get().pluginId
            implementationClass = "com.canerture.convention.AndroidApplicationFirebaseConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.quiz.jvm.library.get().pluginId
            implementationClass = "com.canerture.convention.JvmLibraryConventionPlugin"
        }
    }
}