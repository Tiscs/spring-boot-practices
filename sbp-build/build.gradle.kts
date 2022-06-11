plugins {
    `kotlin-dsl`
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.7.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.0")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
}
