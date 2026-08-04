plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.canerture.feature.favorites.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
}