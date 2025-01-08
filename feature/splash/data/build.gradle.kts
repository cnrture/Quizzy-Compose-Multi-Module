plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
}

android {
    namespace = "com.quiz.feature.splash.data"
}

dependencies {
    implementation(projects.feature.splash.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
}