# Spring Cloud Practices

[![Deploy Parent Status](https://github.com/tiscs/spring-cloud-practices/actions/workflows/deploy-parent.yml/badge.svg)](https://github.com/tiscs/spring-cloud-practices/actions/workflows/deploy-parent.yml)

## Modules

- **scp-parent**:
  Parent Project
- **scp-config**:
  Config Server
- **scp-users**:
  OAuth2 Server
- **scp-mqtt**:
  MQTT Server

## Docker Images

``` sh
$ docker build --rm -f scp-{module}/Dockerfile .
```

## Development

- **scp-config**

``` yaml
# scp-config/src/main/resources/application-local.yml
spring:
  cloud:
    config:
      server:
        native:
          search-locations: file:./config-repo
```

``` sh
$ ./mvnw -f ./scp-config spring-boot:run -Dspring-boot.run.profiles=local,native -Dspring-boot.run.workingDirectory=../ -Dspring-boot.run.arguments="--server.port=8888 --encrypt.key=kbpfrIaimS5AJG4rQPvNEGeX"
```

- **scp-{module}**

``` yaml
# scp-{module}/src/main/resources/application-local.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${spring.application.name}?currentSchema=public
    username: 
    password: 
  config:
    import:
      - file:./config-repo/
      - optional:configserver:http://localhost:8888/
  cloud:
    config:
      enabled: false
```

``` sh
$ ./mvnw -f ./scp-{module} spring-boot:run -Dspring-boot.run.profiles=local,dev -Dspring-boot.run.workingDirectory=../ -Dspring-boot.run.arguments="--server.port=8080"
```
