plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.canerture.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization)
}