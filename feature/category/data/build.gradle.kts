plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.quiz.feature.category.data"
}

dependencies {
    implementation(projects.feature.category.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
}