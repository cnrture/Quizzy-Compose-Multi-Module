plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
}

android {
    namespace = "com.canerture.feature.login.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.login.domain)
}