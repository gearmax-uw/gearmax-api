FROM openjdk:11
EXPOSE 8080
ADD target/gearmax-api.jar gearmax-api.jar
ENTRYPOINT ["java", "-jar", "/gearmax-api.jar"]