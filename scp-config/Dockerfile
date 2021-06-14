FROM maven:3-adoptopenjdk-11-openj9 as build
COPY ./scp-config/ /src/scp-config
WORKDIR /src/scp-config
RUN mvn package -q -B -U -DskipTests

FROM adoptopenjdk:11-jre-openj9
COPY --from=build /src/scp-config/target/scp-config.jar /app/bundle.jar
ENV TIMEZONE=GMT
EXPOSE 8080
CMD [ "java", "-jar", "-Dfile.encoding=UTF-8", "-Duser.timezone=$TIMEZONE", "-Djava.security.egd=file:/dev/./urandom", "/app/bundle.jar", "--server.port=8080" ]
