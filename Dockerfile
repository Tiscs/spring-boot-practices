FROM eclipse-temurin:11-jdk-alpine as build
ARG SBP_MODULE
COPY ./ /src
WORKDIR /src
RUN test -d ./$SBP_MODULE && echo "Building module \"$SBP_MODULE\"" && ./gradlew :$SBP_MODULE:bootJar && mkdir /dst && mv /src/$SBP_MODULE/build/libs/$SBP_MODULE-*.jar /dst/bundle.jar

FROM eclipse-temurin:11-jre-alpine
COPY --from=build /dst/bundle.jar /app/bundle.jar
WORKDIR /app
ENV JAVA_USER_TIMEZONE=GMT SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "-Duser.timezone=$JAVA_USER_TIMEZONE", "-Djava.security.egd=file:/dev/./urandom", "/app/bundle.jar", "--server.port=8080" ]
