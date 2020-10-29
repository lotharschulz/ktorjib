plugins {
    application
    kotlin("jvm") version "1.4.10"
    id("com.google.cloud.tools.jib") version "2.6.0"
}

group = "ktor01"
version = "1.1-SNAPSHOT"


val ktorVersion by extra("1.4.1")
val logbackVersion by extra("1.2.3")

val mainClass by extra("io.ktor.server.netty.EngineMain")

application {
    mainClassName = mainClass

    applicationDefaultJvmArgs = listOf(
            "-server",
            "-Djava.awt.headless=true",
            "-Xms128m",
            "-Xmx256m",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=100"
    )
}

java.sourceCompatibility = JavaVersion.VERSION_12

dependencies {
    implementation(kotlin("stdlib"))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    testCompile ("io.ktor:ktor-server-test-host:$ktorVersion")
}

jib {
    from {
        image = "openjdk:12@sha256:835e400c2620ae21763dc5b00d48df71188ad55fd8bbdcc4ccb5513ef6f35710"
    }
    container {
        ports = listOf("8080")
        mainClass = this@Build_gradle.mainClass
    }
}

repositories {
    jcenter()
    mavenCentral()
}
