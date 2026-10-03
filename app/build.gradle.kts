import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.quiz.android.application)
    alias(libs.plugins.quiz.android.application.compose)
    alias(libs.plugins.quiz.android.firebase)
    alias(libs.plugins.quiz.hilt)
    alias(libs.plugins.kotlinx.serialization.plugin)
}

android {
    namespace = "com.canerture.quizappcompose"
    defaultConfig {
        applicationId = "com.canerture.quizappcompose"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use(::load)
        }
    }
    val keystoreKeys = listOf("KEYSTORE_PATH", "KEY_ALIAS", "KEY_PASSWORD", "KEYSTORE_PASSWORD")
    val hasKeystore = keystoreKeys.all { !localProperties.getProperty(it).isNullOrBlank() }

    signingConfigs {
        // Without the keystore keys (fresh clone, CI) debug falls back to the default debug keystore
        // and release stays unsigned instead of failing configuration.
        if (hasKeystore) {
            listOf(getByName("debug"), create("release")).forEach { config ->
                config.storeFile = file(localProperties.getProperty("KEYSTORE_PATH"))
                config.keyAlias = localProperties.getProperty("KEY_ALIAS")
                config.keyPassword = localProperties.getProperty("KEY_PASSWORD")
                config.storePassword = localProperties.getProperty("KEYSTORE_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            if (hasKeystore) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.network)
    implementation(projects.core.datasource.logout)
    implementation(projects.core.connectivity)

    implementation(projects.feature.welcome.data)
    implementation(projects.feature.splash.data)
    implementation(projects.feature.login.data)
    implementation(projects.feature.register.data)
    implementation(projects.feature.home.data)
    implementation(projects.feature.category.data)
    implementation(projects.feature.search.data)
    implementation(projects.feature.detail.data)
    implementation(projects.feature.quiz.data)
    implementation(projects.feature.leaderboard.data)
    implementation(projects.feature.profile.data)
    implementation(projects.feature.editprofile.data)
    implementation(projects.feature.favorites.data)

    implementation(projects.navigation)
    implementation(libs.navigation.compose)
}