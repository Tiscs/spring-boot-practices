plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.7.20")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.7.4")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.14.RELEASE")
}
