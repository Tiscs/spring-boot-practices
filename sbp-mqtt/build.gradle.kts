plugins {
    id("sbp-build-kotlin-spring-boot-webflux")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-api")
    implementation("io.netty:netty-codec-mqtt")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
