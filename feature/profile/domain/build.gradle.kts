plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.canerture.feature.profile.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}