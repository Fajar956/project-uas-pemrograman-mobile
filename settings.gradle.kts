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
        maven { url = uri("https://jitpack.io") } // Untuk Glide, ChipNavigationBar, dll
        maven { url = uri("https://dl.google.com/dl/android/maven2") } // Untuk Google Services
    }
}

rootProject.name = "TravelApp"
include(":app")