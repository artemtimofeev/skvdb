FROM openjdk:20-jdk
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 4004
ENTRYPOINT ["java", "-jar", "app.jar"]