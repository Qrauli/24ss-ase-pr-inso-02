FROM gradle:8.7-jdk21 AS builder

ARG CI
ARG CI_JOB_TOKEN
ARG CI_PROJECT_ID

WORKDIR /app

COPY build.gradle.kts .

COPY src ./src

RUN gradle bootJar

FROM amazoncorretto:21-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/app.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]
