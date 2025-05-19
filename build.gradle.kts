plugins {
    kotlin("jvm") version "2.1.10"
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
    id ("com.xcporter.metaview") version "0.0.5"
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
    implementation("org.fusesource.jansi:jansi:2.4.0")


}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}


tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    config.setFrom("$rootDir/detekt.yml")
    buildUponDefaultConfig = true
    autoCorrect = true
    reports {
        html.required.set(true)
        txt.required.set(false)
        xml.required.set(false)
    }
}

//
//generateUml {
//    // Diagram drzewa klas
//    classTree {
//        // Folder do analizy
//        target = file("src/main/kotlin") // Analizuj folder z kodem źródłowym
//
//        // Katalog wyjściowy
//        outputDir = file("$buildDir/docs/uml") // Pliki wyjściowe w katalogu build/docs/uml
//
//        // Nazwa pliku wyjściowego
//        outputFile = "classTreeDiagram.puml" // Nazwa pliku diagramu
//
//        // Styl diagramu (PlantUML skinparams)
//        style = listOf("skinparam classBackgroundColor LightBlue", "skinparam classBorderColor Black")
//
//        // Ignorowane klasy lub interfejsy
//        ignoreDelegates = listOf("BaseClass", "IgnoredInterface")
//
//        // Rozdzielanie typów sparametryzowanych
//        splitDelegates = listOf("ParameterizedType")
//    }
//
//    // Diagram drzewa funkcji
//    functionTree {
//        // Folder do analizy
//        target = file("src/main/kotlin") // Analizuj folder z kodem źródłowym
//
//        // Katalog wyjściowy
//        outputDir = file("$buildDir/docs/uml") // Pliki wyjściowe w katalogu build/docs/uml
//
//        // Nazwa pliku wyjściowego
//        outputFile = "functionTreeDiagram.puml" // Nazwa pliku diagramu
//
//        // Styl diagramu (PlantUML skinparams)
//        style = listOf("skinparam functionBackgroundColor LightGreen", "skinparam functionBorderColor Black")
//    }
//}
generateUml {
    classTree {}
}
