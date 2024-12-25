plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.android.library.compose)
}

android {
    namespace = "com.canerture.quiz.navigation"

}

dependencies {
    implementation(libs.navigation.compose)

    implementation(projects.feature.splash.ui)
    implementation(projects.feature.welcome.ui)
    implementation(projects.feature.login.ui)
    implementation(projects.feature.register.ui)
    implementation(projects.feature.home.ui)
}