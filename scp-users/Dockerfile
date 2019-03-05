FROM maven:3.6.0-jdk-11 as build
COPY ./scp-users/ /src/scp-users
WORKDIR /src/scp-users
RUN mvn package

FROM openjdk:11-jdk
COPY --from=build /src/scp-config/target/scp-users.jar /app/bundle.jar
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "/app/bundle.jar", "--server.port=8080" ]
