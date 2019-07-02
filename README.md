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

## Development:

``` yaml
# scp-config/src/main/resources/bootstrap-local.yml
spring:
  profiles:
    include: native
  cloud:
    config:
      server:
        native:
          search-locations: {PATH_TO_LOCAL_CONFIG_REPO} # file:///${user.home}/config-repo
```

``` yaml
# scp-{module}/src/main/resources/bootstrap-local.yml
spring:
  profiles:
    include: dev
  datasource:
    username: root
    password: root@local
  cloud:
    config:
      uri: http://localhost:8888
```

``` sh
$ mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.include=local,--server.port=8080
```