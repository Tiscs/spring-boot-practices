import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "io.github.tiscs"
version = "1.0.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

private object Versions {
    const val springdoc = "1.6.6"
    const val exposed = "0.37.3"
    const val mybatisSpringBoot = "2.2.2"
    const val pgsqlDriver = "42.3.2"
    const val swaggerCore = "2.1.12"
}

dependencies {
    constraints {
        implementation("org.springdoc:springdoc-openapi-webmvc-core:${Versions.springdoc}")
        implementation("org.springdoc:springdoc-openapi-webflux-core:${Versions.springdoc}")
        implementation("org.springdoc:springdoc-openapi-security:${Versions.springdoc}")
        implementation("org.springdoc:springdoc-openapi-kotlin:${Versions.springdoc}")
        implementation("org.jetbrains.exposed:exposed-core:${Versions.exposed}")
        implementation("org.jetbrains.exposed:exposed-java-time:${Versions.exposed}")
        implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${Versions.exposed}")
        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:${Versions.mybatisSpringBoot}")
        implementation("org.postgresql:postgresql:${Versions.pgsqlDriver}")
        implementation("io.swagger.core.v3:swagger-annotations:${Versions.swaggerCore}")
    }

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
    archiveClassifier.set("boot")
}
