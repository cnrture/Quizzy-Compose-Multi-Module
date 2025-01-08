import java.util.Properties

plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.android.firebase)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.canerture.core.network"

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
            "BASE_URL",
            "\"${localProperties.getProperty("BASE_URL") ?: "BASE_URL"}\"",
        )
    }
}

dependencies {
    implementation(libs.kotlinx.serialization)
    api(libs.retrofit)
    implementation(libs.converter.kotlinx.serialization)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
}