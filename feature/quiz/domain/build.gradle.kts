plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.canerture.feature.quiz.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
}