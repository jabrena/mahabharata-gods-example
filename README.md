# Mahabharata Gods

REST API to Calculate the popularity of Indian gods in Mahabharata book.
The goal of the project/challenge is to explore **Spring AOT**, **GraalVM Native Images** and **CompletableFutures** in
java.

## Pre-requisites

- Java 25 (intentional development-baseline break; verified with `Temurin 25.0.3`, declared in `.sdkmanrc`)
- Docker (To build a light container with native application)
- GraalVM SDK for Java 25 (To build native image locally, can be installed with SDKMan, e.g. `25.0.2-graalce`)
- httpie (Optional to test API)

## Build locally JVM version

```bash
./mvnw --batch-mode --no-transfer-progress clean verify
```

## Run locally JVM version

```bash
./mvnw spring-boot:run
```

## Build and run locally native version

```bash
./mvnw -Pnative -DskipTests clean native:compile
./target/mahabharata-gods-native
```

## Build and run using docker container with JVM

```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=mahabharata-gods-native:25-jvm
docker run --rm -p 8080:8080 mahabharata-gods-native:25-jvm
```

## Build and run using docker container with native image

```bash
./mvnw spring-boot:build-image -Pnative -Dspring-boot.build-image.imageName=mahabharata-gods-native:25-native
docker run --rm -p 8080:8080 mahabharata-gods-native:25-native
```

JVM and native images use distinct tags (`25-jvm` / `25-native`) so building one does not overwrite the other.

## Pointing at downstream services

The three downstream data sources are configured through Spring's simple discovery client keys and can be overridden
per environment (for example to run against controlled fixtures) with environment variables:

```bash
SPRING_CLOUD_DISCOVERY_CLIENT_SIMPLE_INSTANCES_INDIAN_GOD_SERVICE_0_URI=http://localhost:8090
SPRING_CLOUD_DISCOVERY_CLIENT_SIMPLE_INSTANCES_INDIAN_GODS_SERVICE_0_URI=http://localhost:8090
SPRING_CLOUD_DISCOVERY_CLIENT_SIMPLE_INSTANCES_MAHABHARATA_DATA_SOURCE_SERVICE_0_URI=http://localhost:8090
```

By default they point at `https://en.wikipedia.org`, `https://my-json-server.typicode.com`, and `https://archive.org`
respectively (see `src/main/resources/application.yml`).

## Test

```bash
http :8080/top-gods
```

## Sources

- https://gitlab.com/lealoureiro/mahabharata-gods-native