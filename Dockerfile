ARG DOCKER_MIRROR=docker.io
FROM $DOCKER_MIRROR/eclipse-temurin:21-jdk as build
ARG SBP_MODULE
COPY ./ /src
WORKDIR /src
RUN test -d ./$SBP_MODULE && echo "Building module \"$SBP_MODULE\"" && ./gradlew :$SBP_MODULE:bootJar -x test -x processAot && mkdir /dst && mv /src/$SBP_MODULE/build/libs/$SBP_MODULE-*-boot.jar /dst/bundle.jar

ARG DOCKER_MIRROR=docker.io
FROM $DOCKER_MIRROR/eclipse-temurin:21-jre
COPY --from=build /dst/bundle.jar /app/bundle.jar
WORKDIR /app
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=utf-8", "-Djava.security.egd=file:/dev/./urandom", "/app/bundle.jar", "--server.port=8080" ]
