plugins {
    application
    kotlin("jvm") version "1.3.50"
    id("com.google.cloud.tools.jib") version "1.6.1"
}

group = "ktor01"
version = "1.0-SNAPSHOT"


val ktorVersion by extra("1.2.5")
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
        image = "openjdk:12"
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
