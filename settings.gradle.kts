pluginManagement {
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
// plugins {
//     id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
// }
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Vedika"
include(":app")
include(":core:design")
include(":core:navigation")
include(":core:data")
include(":feature:auth")
include(":feature:dashboard")
include(":feature:calendar")
include(":feature:inventory")
include(":feature:finance")
include(":feature:gallery")

