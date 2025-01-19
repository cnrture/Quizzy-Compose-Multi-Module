plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.quiz.feature.search.domain"

dependencies {
    implementation(projects.core.common)
    implementation(libs.javax.inject)
}