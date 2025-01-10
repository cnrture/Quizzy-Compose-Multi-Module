plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
}

android {
    namespace = "com.canerture.feature.detail.ui"
}

dependencies {
    implementation(projects.feature.detail.domain)
}