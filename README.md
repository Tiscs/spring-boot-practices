# Spring Boot Practices

## Modules

- **sbp-build**:
  Gradle plugins
- **sbp-config**:
  Config files
- **sbp-users**:
  Users service
- **sbp-mqtt**:
  MQTT server

## Docker Images

``` sh
$ docker build --build-arg SBP_MODULE=sbp-{module} .
```

## Development

``` yaml
# sbp-{module}/src/main/resources/application-local.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${spring.application.name}?currentSchema=public
    username:
    password:
  config:
    import:
      - file:../sbp-config/
```

``` sh
# https://docs.spring.io/spring-boot/docs/2.6.2/gradle-plugin/reference/htmlsingle/#running-your-application
$ ./gradlew :sbp-{module}:bootRun --args="--spring.profiles.active=dev,local --server.port=8080"
```
