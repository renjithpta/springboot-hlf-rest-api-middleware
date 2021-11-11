FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
EXPOSE 8080
EXPOSE 9000
COPY connection/connection-org1.json /user/connection-org1.json
COPY target/fixmtoacrisrestapi-1.0.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]