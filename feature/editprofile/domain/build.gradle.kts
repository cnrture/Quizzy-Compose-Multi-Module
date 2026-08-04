plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.canerture.feature.editprofile.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}