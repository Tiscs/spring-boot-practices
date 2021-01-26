# Spring Cloud Practices

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
# scp-config/src/main/resources/bootstrap-local.yml
spring:
  cloud:
    config:
      server:
        native:
          search-locations: {PATH_TO_LOCAL_CONFIG_REPO} # file:///${user.home}/config-repo
```

``` sh
$ mvn spring-boot:run -Dspring-boot.run.profiles=local,native -Dspring-boot.run.arguments="--server.port=8888 --encrypt.key=kbpfrIaimS5AJG4rQPvNEGeX"
```

- **scp-{module}**

``` yaml
# scp-{module}/src/main/resources/bootstrap-local.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${spring.application.name}?currentSchema=public
    username: 
    password: 
```

``` sh
$ mvn spring-boot:run -Dspring-boot.run.profiles=local,dev -Dspring-boot.run.arguments="--server.port=8080 --spring.config.import=configserver:http://localhost:8888/"
```
