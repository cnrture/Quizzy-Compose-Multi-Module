plugins {
    alias(libs.plugins.quiz.android.library.compose)
    alias(libs.plugins.quiz.android.feature)
}

android {
    namespace = "com.canerture.feature.register.ui"
}

dependencies {
    implementation(projects.feature.register.domain)
}