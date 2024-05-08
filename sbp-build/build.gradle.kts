plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:2.0.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.2.5")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.5")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("org.graalvm.buildtools.native:org.graalvm.buildtools.native.gradle.plugin:0.10.2")
}
