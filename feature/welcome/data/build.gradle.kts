import java.util.Properties

plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.android.firebase)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.quiz.feature.welcome.data"

    val localProperties = Properties().apply {
        val propsFile = rootProject.file("local.properties")
        if (propsFile.exists()) {
            load(propsFile.inputStream())
        }
    }

    defaultConfig {
        buildFeatures {
            buildConfig = true
        }

        buildConfigField(
            "String",
            "SERVER_CLIENT_ID",
            "\"${localProperties.getProperty("SERVER_CLIENT_ID") ?: "SERVER_CLIENT_ID"}\""
        )
    }
    kotlin {
        compilerOptions {
            freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
}

dependencies {
    implementation(projects.feature.welcome.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.datasource.profile)
}