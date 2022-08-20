plugins {
    `kotlin-dsl`
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.7.10")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.3")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.13.RELEASE")
}
