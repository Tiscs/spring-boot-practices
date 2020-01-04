FROM maven:3.6.2-jdk-11 as build
COPY ./scp-mqtt/ /src/scp-mqtt
WORKDIR /src/scp-mqtt
RUN mvn package -q -U -DskipTests

FROM openjdk:11-jdk
COPY --from=build /src/scp-mqtt/target/scp-mqtt.jar /app/bundle.jar
ENV TIMEZONE=GMT
EXPOSE 8080 1883
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "-Duser.timezone=$TIMEZONE", "/app/bundle.jar", "--server.port=8080" ]
