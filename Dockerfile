# Build stage
FROM gradle:7.1-jdk11 AS build
WORKDIR /project
COPY src ./src/
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradle.properties ./
RUN gradle clean && gradle installDist

# Run stage
FROM openjdk:11
WORKDIR /server
COPY --from=build /project/build/install/shop-openapi-server ./serverDist
CMD ./serverDist/bin/shop-openapi-server
