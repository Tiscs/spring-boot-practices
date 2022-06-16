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

val springDocVersion: String by project
val exposedVersion: String by project
val mybatisSpringVersion: String by project
val postgresqlDriverVersion: String by project
val swaggerCoreVersion: String by project
val openFeignVersion: String by project

dependencies {
    constraints {
        implementation("org.springdoc:springdoc-openapi-webmvc-core:${springDocVersion}")
        implementation("org.springdoc:springdoc-openapi-webflux-core:${springDocVersion}")
        implementation("org.springdoc:springdoc-openapi-security:${springDocVersion}")
        implementation("org.springdoc:springdoc-openapi-kotlin:${springDocVersion}")
        implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
        implementation("org.jetbrains.exposed:exposed-java-time:${exposedVersion}")
        implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${exposedVersion}")
        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:${mybatisSpringVersion}")
        implementation("org.postgresql:postgresql:${postgresqlDriverVersion}")
        implementation("io.swagger.core.v3:swagger-annotations:${swaggerCoreVersion}")
        implementation("io.github.openfeign:feign-core:${openFeignVersion}")
        implementation("io.github.openfeign:feign-jackson:${openFeignVersion}")
        implementation("io.github.openfeign:feign-slf4j:${openFeignVersion}")
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
