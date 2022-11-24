plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.21")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.7.21")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.5")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.0")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.1")
}
