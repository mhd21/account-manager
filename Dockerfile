FROM openjdk:17-jdk-alpine

WORKDIR /usr/src/app

COPY . .

RUN ./mvnw install -DskipTests

# RUN ./mvnw package -DskipTests


# RUN mkdir build

# RUN cp target/*.jar build/app.jar

# ENTRYPOINT ["java","-jar","build/app.jar"]

ENTRYPOINT ["./mvnw","spring-boot:run"]

EXPOSE 8080