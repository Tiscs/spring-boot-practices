FROM maven:3-adoptopenjdk-11-openj9 as build
COPY ./scp-users/ /src/scp-users
WORKDIR /src/scp-users
RUN mvn package -B -U -DskipTests

FROM adoptopenjdk:11-jre-openj9
COPY --from=build /src/scp-users/target/scp-users.jar /app/bundle.jar
ENV JAVA_USER_TIMEZONE=GMT \
    SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "-Duser.timezone=$JAVA_USER_TIMEZONE", "-Djava.security.egd=file:/dev/./urandom", "/app/bundle.jar", "--server.port=8080" ]
