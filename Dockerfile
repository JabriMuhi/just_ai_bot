FROM gradle:7.5.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

COPY .env /app/.env
WORKDIR /app

RUN ls -la /app/.env

ENTRYPOINT ["java","-jar","/app/app.jar"]
