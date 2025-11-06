plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    // 🧩 Add Dokka plugin
    id("org.jetbrains.dokka") version "1.9.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Validation (Bean Validation)
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database drivers
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2") // optional dev DB

    // Kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

noArg {
    annotation("jakarta.persistence.Entity")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    mainClass.set("APiRes.APiReservationApplicationKt")
}

// 🧱 Dokka configuration
tasks.dokkaHtml {
    // ✅ Fix deprecated buildDir warning
    outputDirectory.set(layout.buildDirectory.dir("dokka"))
    dokkaSourceSets {
        named("main") {
            displayName.set("API Reservation Documentation")

            // Optional GitHub link (customize your URL)
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(
                    uri("https://github.com/yourusername/yourrepo/tree/main/src/main/kotlin").toURL()
                )
                remoteLineSuffix.set("#L")
            }
        }
    }
}

// 🧩 Fix Jackson version conflict between Spring Boot and Dokka
configurations.configureEach {
    if (name.startsWith("dokka")) {
        resolutionStrategy.eachDependency {
            if (requested.group == "com.fasterxml.jackson.core" ||
                requested.group == "com.fasterxml.jackson.module" ||
                requested.group == "com.fasterxml.jackson.datatype" ||
                requested.group == "com.fasterxml.jackson.dataformat") {
                useVersion("2.15.2")
                because("Avoid Jackson 2.17.x conflict between Dokka and Spring Boot")
            }
        }
    }
}
