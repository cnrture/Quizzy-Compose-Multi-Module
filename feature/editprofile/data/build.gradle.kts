plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.quiz.feature.editprofile.data"
}

dependencies {
    implementation(projects.feature.editprofile.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
    implementation(projects.core.datasource.profile)
}