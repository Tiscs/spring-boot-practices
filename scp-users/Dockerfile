FROM maven:3.6.3-jdk-11 as build
COPY ./scp-users/ /src/scp-users
WORKDIR /src/scp-users
RUN mvn package -q -U -DskipTests

FROM openjdk:11-jdk
COPY --from=build /src/scp-users/target/scp-users.jar /app/bundle.jar
ENV TIMEZONE=GMT
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "-Duser.timezone=$TIMEZONE", "-Djava.security.egd=file:/dev/./urandom", "/app/bundle.jar", "--server.port=8080" ]
