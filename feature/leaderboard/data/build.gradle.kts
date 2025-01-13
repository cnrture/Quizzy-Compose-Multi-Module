plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.quiz.retrofit)
}

android {
    namespace = "com.quiz.feature.leaderboard.data"
}

dependencies {
    implementation(projects.feature.leaderboard.domain)
    implementation(projects.core.network)
    implementation(projects.core.common)
}