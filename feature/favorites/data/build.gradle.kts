plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.quiz.feature.favorites.data"
}

dependencies {
    implementation(projects.feature.favorites.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
}