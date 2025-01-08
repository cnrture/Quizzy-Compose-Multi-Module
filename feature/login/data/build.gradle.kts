plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.quiz.feature.login.data"
}

dependencies {
    implementation(projects.feature.login.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
}