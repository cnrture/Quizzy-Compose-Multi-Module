plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.android.firebase)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.canerture.core.network"

    defaultConfig {
        buildFeatures {
            buildConfig = true
        }

        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://api.canerture.com/quiz/\"",
        )
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.datasource.logout)
}