plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.quiz.feature.category.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
}