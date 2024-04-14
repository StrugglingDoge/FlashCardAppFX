plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = "com.flashcardapp"
version = "1.0"

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    
    implementation("commons-codec:commons-codec:1.15")

    implementation("org.json:json:20240303")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainModule.set("com.flashcardapp")
    mainClass.set("com.flashcardapp.FlashcardApp")
}

