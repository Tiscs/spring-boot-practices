ARG DOCKER_MIRROR=docker.io
FROM $DOCKER_MIRROR/eclipse-temurin:17-jdk-alpine as build
ARG SBP_MODULE
COPY ./ /src
WORKDIR /src
RUN test -d ./$SBP_MODULE && echo "Building module \"$SBP_MODULE\"" && ./gradlew :$SBP_MODULE:bootJar -x test && mkdir /dst && mv /src/$SBP_MODULE/build/libs/$SBP_MODULE-*-boot.jar /dst/bundle.jar

ARG DOCKER_MIRROR=docker.io
FROM $DOCKER_MIRROR/eclipse-temurin:17-jre-alpine
COPY --from=build /dst/bundle.jar /app/bundle.jar
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=utf-8", "-Djava.security.egd=file:/dev/./urandom", "/app/bundle.jar", "--server.port=8080" ]
