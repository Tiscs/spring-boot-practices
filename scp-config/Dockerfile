FROM maven:3.6.1-jdk-11 as build
COPY ./scp-config/ /src/scp-config
WORKDIR /src/scp-config
RUN mvn package

FROM openjdk:11-jdk
COPY --from=build /src/scp-config/target/scp-config.jar /app/bundle.jar
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "/app/bundle.jar", "--server.port=8080" ]
