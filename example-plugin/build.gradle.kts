plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "me.tech"
version = "0.0.1"

java { toolchain.languageVersion.set(JavaLanguageVersion.of(project.ext.get("javaToolchainVersion") as Int)) }

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":api"))

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}

bukkit {
    name = "internal-bosses-example"
    description = "Plugin to test the API."
    apiVersion = "1.18"
    author = "Tech"
    main = "me.tech.internalbosses.exampleplugin.ExamplePlugin"
    depend = listOf("internal-bosses")
}