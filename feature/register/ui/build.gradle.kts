plugins {
    alias(libs.plugins.quiz.android.library.compose)
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.test)
}

android {
    namespace = "com.canerture.feature.register.ui"
}

dependencies {
    implementation(projects.feature.register.domain)
    testImplementation(projects.core.testing)
}