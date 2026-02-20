plugins {
    id("sbp-build-kotlin-spring-boot-webmvc")
}

dependencies {
    implementation("tools.jackson.dataformat:jackson-dataformat-yaml")
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("org.jetbrains.exposed:exposed-java-time")
    implementation("org.jetbrains.exposed:exposed-spring-boot4-starter")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.security:spring-security-oauth2-authorization-server")
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.integration:spring-integration-redis")
    implementation("io.lettuce:lettuce-core")
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api")
    implementation("io.github.openfeign:feign-core")
    implementation("io.github.openfeign:feign-jackson")
    implementation("io.github.openfeign:feign-slf4j")
    testImplementation("com.h2database:h2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

dependencies {
    // https://github.com/spring-projects/spring-framework/blob/v7.0.5/framework-platform/framework-platform.gradle#L12
    implementation("io.netty:netty-resolver-dns-native-macos:4.2.10.Final:osx-x86_64")
    implementation("io.netty:netty-resolver-dns-native-macos:4.2.10.Final:osx-aarch_64")
}
