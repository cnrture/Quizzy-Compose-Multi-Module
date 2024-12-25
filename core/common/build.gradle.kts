plugins {
    alias(libs.plugins.quiz.jvm.library)
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.canerture.core.common"

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.material3)
    implementation(libs.lifecycle.runtime.compose)
}