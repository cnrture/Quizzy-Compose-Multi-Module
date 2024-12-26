plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
}

android {
    namespace = "com.canerture.feature.welcome.ui"
}

dependencies {
    implementation(projects.feature.welcome.domain)
    implementation(projects.core.common)
}