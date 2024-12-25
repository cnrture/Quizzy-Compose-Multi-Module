pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QuizAppCompose"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:network")
include(":core:ui")
include(":feature:splash:ui")
include(":feature:welcome:ui")
include(":core:common")
include(":feature:login:ui")
include(":feature:register:ui")
include(":feature:register:data")
include(":feature:register:domain")
include(":feature:login:data")
include(":feature:login:domain")
