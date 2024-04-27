FROM gradle:8.7-jdk21 AS builder

WORKDIR /app

COPY build.gradle.kts .

COPY src ./src

RUN gradle bootJar

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/app.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
