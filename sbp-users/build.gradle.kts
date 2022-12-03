plugins {
    id("sbp-build-kotlin-spring-boot-webflux")
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("org.jetbrains.exposed:exposed-java-time")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.integration:spring-integration-redis")
    implementation("io.lettuce:lettuce-core")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api")
    implementation("io.github.openfeign:feign-core")
    implementation("io.github.openfeign:feign-jackson")
    implementation("io.github.openfeign:feign-slf4j")
    implementation("co.elastic.logging:logback-ecs-encoder")
    testImplementation("com.h2database:h2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
