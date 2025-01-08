plugins {
    alias(libs.plugins.quiz.android.library)
    alias(libs.plugins.quiz.hilt)
}

android {
    namespace = "com.canerture.core.network"
}

dependencies {
    implementation(libs.datastore)
}