@file:Suppress("UnstableApiUsage")

include(":ads")


include(":firebase")


include(":database")


pluginManagement {
    repositories {
        google()
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

rootProject.name = "MaxPad"
include(":app", ":lib", ":network")