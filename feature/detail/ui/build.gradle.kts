plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
    alias(libs.plugins.quiz.test)
}

android {
    namespace = "com.canerture.feature.detail.ui"
}

dependencies {
    implementation(projects.feature.detail.domain)
    testImplementation(projects.core.testing)
}