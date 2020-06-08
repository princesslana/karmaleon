plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.50")
    id("com.diffplug.gradle.spotless").version("3.28.1")
    application
    id("com.github.johnrengelman.shadow").version("5.2.0")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.github.princesslana:smalld:0.2.3")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    implementation("com.google.code.gson:gson:2.8.0")
    implementation("org.slf4j:slf4j-simple:1.7.29")
    implementation("com.michael-bull.kotlin-retry:kotlin-retry:1.0.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "com.github.princesslana.karmaleon.KarmaleonKt"
}

spotless {
    val version = "0.36.0"
    val config = mapOf(
        "max_line_length" to "88",
        "disabled_rules" to "no-wildcard-imports")

    kotlin {
        ktlint(version).userData(config)
    }
    kotlinGradle {
        target("build.gradle.kts")
        ktlint(version).userData(config)
    }
}
