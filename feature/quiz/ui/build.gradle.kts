plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
}

android {
    namespace = "com.canerture.feature.quiz.ui"
}

dependencies {
    implementation(projects.feature.quiz.domain)
}