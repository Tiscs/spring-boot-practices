import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("sbp-build-kotlin-library-base")
    id("org.graalvm.buildtools.native")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val springDocVersion: String by project
val springOAuth2ServerVersion: String by project
val grpcSpringVersion: String by project
val exposedVersion: String by project
val mybatisSpringVersion: String by project
val postgresqlDriverVersion: String by project
val openFeignVersion: String by project
val logbackECSEncoderVersion: String by project

dependencies {
    constraints {
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:${springDocVersion}")
        implementation("org.springdoc:springdoc-openapi-starter-webflux-api:${springDocVersion}")
        implementation("org.springframework.security:spring-security-oauth2-authorization-server:${springOAuth2ServerVersion}")
        implementation("net.devh:grpc-spring-boot-starter:${grpcSpringVersion}")
        implementation("net.devh:grpc-server-spring-boot-starter:${grpcSpringVersion}")
        implementation("net.devh:grpc-client-spring-boot-starter:${grpcSpringVersion}")
        implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
        implementation("org.jetbrains.exposed:exposed-java-time:${exposedVersion}")
        implementation("org.jetbrains.exposed:exposed-spring-boot-starter:${exposedVersion}")
        implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:${mybatisSpringVersion}")
        implementation("org.postgresql:postgresql:${postgresqlDriverVersion}")
        implementation("io.github.openfeign:feign-core:${openFeignVersion}")
        implementation("io.github.openfeign:feign-jackson:${openFeignVersion}")
        implementation("io.github.openfeign:feign-slf4j:${openFeignVersion}")
        implementation("co.elastic.logging:logback-ecs-encoder:${logbackECSEncoderVersion}")
    }
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<BootJar>("bootJar") {
    archiveClassifier.set("boot")
}
