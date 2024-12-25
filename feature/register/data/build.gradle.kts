plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.android.firebase)
    alias(libs.plugins.quiz.hilt)
}

android {
    namespace = "com.quiz.feature.register.data"
}

dependencies {
    implementation(projects.feature.register.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
}