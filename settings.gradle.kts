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

rootProject.name = "Quizzy"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:network")
include(":core:ui")
include(":core:common")
include(":core:datastore")
include(":feature:welcome:data")
include(":feature:welcome:domain")
include(":feature:welcome:ui")
include(":feature:splash:data")
include(":feature:splash:domain")
include(":feature:splash:ui")
include(":feature:login:data")
include(":feature:login:domain")
include(":feature:login:ui")
include(":feature:register:data")
include(":feature:register:domain")
include(":feature:register:ui")
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:ui")
include(":feature:favorites:ui")
include(":feature:leaderboard:ui")
include(":feature:profile:ui")
include(":feature:detail:data")
include(":feature:detail:domain")
include(":feature:detail:ui")
include(":feature:template:ui")
include(":feature:template:data")
include(":feature:template:domain")
include(":navigation")
include(":core:datasource")
include(":core:datasource:logout")
include(":core:connectivity")
