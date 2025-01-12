plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.canerture.feature.quiz.data"
}

dependencies {
    implementation(projects.feature.quiz.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
}