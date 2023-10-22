@file:Suppress("UnstableApiUsage")

include(":billing")


include(":app")

include(":lib")

include(":network")

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