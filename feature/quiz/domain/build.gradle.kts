plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.quiz.feature.quiz.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
}