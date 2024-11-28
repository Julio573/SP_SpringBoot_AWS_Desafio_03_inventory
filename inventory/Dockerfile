FROM openjdk:21-slim
WORKDIR /app
COPY target/inventory-0.0.1-SNAPSHOT.jar inventory.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "inventory.jar"]