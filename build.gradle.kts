import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("io.papermc.paperweight.userdev") version "1.3.8"
}

group = "me.dolphin2410"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    compileOnly("io.github.monun:tap-api:4.8.0")
    compileOnly("io.github.monun:kommand-api:2.14.0")
    compileOnly("io.github.monun:invfx-api:3.2.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}