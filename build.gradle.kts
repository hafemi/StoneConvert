plugins {
    id("java")
}

group = "io.hafemi"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("StoneConvert")
    archiveVersion.set("")
}