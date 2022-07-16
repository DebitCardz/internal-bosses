plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "me.tech"
version = project.version

java { toolchain.languageVersion.set(JavaLanguageVersion.of(project.ext.get("javaToolchainVersion") as Int)) }

repositories {
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":api"))

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    implementation("net.kyori:adventure-text-minimessage:4.11.0")

    compileOnly("com.github.decentsoftware-eu:decentholograms:2.5.1")
    compileOnly("com.gmail.filoghost.holographicdisplays:holographicdisplays-api:2.4.9")

    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}

bukkit {
    name = "internal-bosses"
    description = "Basic boss plugin."
    apiVersion = "1.18"
    author = "Tech"
    main = "me.tech.internalbosses.plugin.InternalBossesPlugin"
    softDepend = listOf("HolographicDisplays", "DecentHolograms")

    commands {
        register("summonboss")
    }
}