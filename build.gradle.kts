plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.easymock:easymock:5.2.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

javafx {
    version = "17.0.10"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ui.Main")
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
}