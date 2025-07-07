plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "io.github.tiscs"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        javaParameters = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
