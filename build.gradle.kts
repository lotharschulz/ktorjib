plugins {
    application
    kotlin("jvm") version "1.5.21"
    id("com.google.cloud.tools.jib") version "3.1.4"
}

group = "ktor01"
version = "1.2-SNAPSHOT"


val ktorVersion by extra("1.6.2")
val logbackVersion by extra("1.2.5")

val mainClassStr by extra("io.ktor.server.netty.EngineMain")

application {
    mainClassName = mainClassStr

    applicationDefaultJvmArgs = listOf(
            "-server",
            "-Djava.awt.headless=true",
            "-Xms128m",
            "-Xmx256m",
            "-XX:+UseG1GC",
            "-XX:MaxGCPauseMillis=100"
    )
}

java {
    sourceCompatibility = JavaVersion.VERSION_12
    targetCompatibility = JavaVersion.VERSION_12
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
}

jib {
    from {
        image = "openjdk:12@sha256:835e400c2620ae21763dc5b00d48df71188ad55fd8bbdcc4ccb5513ef6f35710"
    }
    container {
        ports = listOf("8080")
        mainClass = this@Build_gradle.mainClassStr
    }
}

repositories {
    jcenter()
    mavenCentral()
}
