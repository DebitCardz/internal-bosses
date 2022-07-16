plugins {
    id("java")
}

group = "me.tech"
version = project.version

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
}