import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "io.github.tiscs"
version = "1.0.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val springDocVersion: String by project
val exposedVersion: String by project
val mybatisSpringVersion: String by project
val postgresqlDriverVersion: String by project
val swaggerCoreVersion: String by project
val openFeignVersion: String by project
val logbackECSEncoderVersion: String by project

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
        implementation("co.elastic.logging:logback-ecs-encoder:${logbackECSEncoderVersion}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
