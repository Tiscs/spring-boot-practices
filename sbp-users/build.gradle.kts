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
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.integration:spring-integration-redis")
    implementation("io.lettuce:lettuce-core")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api")
    implementation("io.github.openfeign:feign-core")
    implementation("io.github.openfeign:feign-jackson")
    implementation("io.github.openfeign:feign-slf4j")
    implementation("co.elastic.logging:logback-ecs-encoder")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.87.Final:osx-x86_64")
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.87.Final:osx-aarch_64")
    testImplementation("com.h2database:h2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
