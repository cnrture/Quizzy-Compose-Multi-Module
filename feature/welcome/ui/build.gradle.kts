plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
    alias(libs.plugins.quiz.test)
}

android {
    namespace = "com.canerture.feature.welcome.ui"
}

dependencies {
    implementation(projects.feature.welcome.domain)
    testImplementation(projects.core.testing)
}