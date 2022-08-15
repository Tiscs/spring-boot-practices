plugins {
    id("sbp-build-kotlin-spring-boot-webflux")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-kotlin")
    implementation("org.springdoc:springdoc-openapi-webflux-core")
    implementation("io.netty:netty-codec-mqtt")
    implementation("co.elastic.logging:logback-ecs-encoder")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
