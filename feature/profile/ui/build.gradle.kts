plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
    alias(libs.plugins.quiz.test)
}

android {
    namespace = "com.canerture.feature.profile.ui"
}

dependencies {
    implementation(projects.feature.profile.domain)
    testImplementation(projects.core.testing)
}