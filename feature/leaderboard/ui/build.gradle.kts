plugins {
    alias(libs.plugins.quiz.android.feature)
    alias(libs.plugins.quiz.android.library.compose)
}

android {
    namespace = "com.canerture.feature.leaderboard.ui"
}

dependencies {
    implementation(projects.feature.leaderboard.domain)
}