pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "internal-bosses"
include("api")
include("plugin")
include("example-plugin")
