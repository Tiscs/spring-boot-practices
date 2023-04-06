plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.8.20")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.0.5")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.0")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.2")
    implementation("org.graalvm.buildtools.native:org.graalvm.buildtools.native.gradle.plugin:0.9.20")
}
