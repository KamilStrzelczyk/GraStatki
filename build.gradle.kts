plugins {
    kotlin("jvm") version "2.1.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
}

group = "org.ks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // Dodaj plugin formatowania
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

// Konfiguracja Detekta
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    config.setFrom("$rootDir/detekt.yml") // Możesz dostosować konfigurację
    buildUponDefaultConfig = true
    autoCorrect = true // ✅ włączenie autoformatowania
    reports {
        html.required.set(true)
        txt.required.set(false)
        xml.required.set(false)
    }
}
