plugins {
    alias(libs.plugins.quiz.jvm.library)
}

group = "com.canerture.core.testing"

dependencies {
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
}
