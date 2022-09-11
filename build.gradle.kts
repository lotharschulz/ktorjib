plugins {
    application
    kotlin("jvm") version "1.6.21"
    id("com.google.cloud.tools.jib") version "3.3.0"
}

group = "ktor01"
version = "1.2-SNAPSHOT"

val ktorVersion by extra("2.1.0")
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
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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
        image = "openjdk:17@sha256:528707081fdb9562eb819128a9f85ae7fe000e2fbaeaf9f87662e7b3f38cb7d8"
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
