# development-platform-baseline Specification

## Purpose
TBD - created by archiving change modernize-java-25-spring-boot-4-1. Update Purpose after archive.
## Requirements
### Requirement: Java and Spring Boot baseline
The project SHALL compile and run with Java 25 and SHALL use Spring Boot 4.1.0. Its dependencies and Maven plugins MUST form a compatible build configuration for that baseline.

#### Scenario: Maven configuration targets the required platform
- **WHEN** the effective Maven project configuration is inspected
- **THEN** it targets Java 25 and Spring Boot 4.1.0
- **AND** its dependency and plugin resolution completes without baseline compatibility errors

### Requirement: Reproducible Maven verification
The project SHALL complete its full clean Maven verification lifecycle on Java 25.

#### Scenario: Clean verification succeeds
- **WHEN** a contributor using Java 25 runs `./mvnw --batch-mode --no-transfer-progress clean verify`
- **THEN** the command completes successfully

### Requirement: Consistent Java toolchain configuration
The Maven configuration, SDKMAN configuration, and GitHub Actions workflow SHALL consistently select Java 25.

#### Scenario: Local, build, and CI declarations agree
- **WHEN** the Java versions in `pom.xml`, `.sdkmanrc`, and the GitHub Actions workflow are inspected
- **THEN** every declaration specifies Java 25

### Requirement: Top-gods behavior remains compatible
The modernized application SHALL preserve the existing `GET /top-gods` JSON endpoint and SHALL return the three valid gods with the highest occurrence counts in descending rank order.

#### Scenario: Endpoint returns the existing top-three result
- **GIVEN** the three downstream roles return the existing controlled test inputs without using live internet services
- **WHEN** a client sends `GET /top-gods` to the modernized application
- **THEN** the response status is 200
- **AND** the response content type is `application/json`
- **AND** the response body contains exactly these objects in this order: `{"name":"Brahma","hitCount":8100}`, `{"name":"Rama","hitCount":845}`, and `{"name":"Hanuman","hitCount":54}`

### Requirement: External service boundaries remain compatible
The modernized application SHALL retain the Indian-gods list, Indian-god validation, and Mahabharata text-source roles, their existing HTTP paths, and the existing `spring.cloud.discovery.client.simple.instances.*[0].uri` configuration keys. Removing the Spring Cloud test dependency MUST NOT rename or remove those deployment inputs.

#### Scenario: Controlled service locations preserve all three integrations
- **GIVEN** controlled service locations are supplied through the three existing `spring.cloud.discovery.client.simple.instances.*[0].uri` keys
- **WHEN** a client sends `GET /top-gods`
- **THEN** the application requests the Indian-gods list from `/jabrena/latency-problems/indian`
- **AND** validates candidate names through `/wiki/{indianGod}`
- **AND** requests the Mahabharata text from `/stream/TheMahabharataOfKrishna-dwaipayanaVyasa/MahabharataOfVyasa-EnglishTranslationByKMGanguli_djvu.txt`
- **AND** returns the existing top-three JSON response

### Requirement: Native executable support
The project SHALL support Spring AOT and native executable compilation with a Java 25-compatible GraalVM distribution.

#### Scenario: Documented native build succeeds
- **GIVEN** the documented Java 25-compatible GraalVM distribution and controlled downstream fixtures are available
- **WHEN** the documented native-image build command is run and the produced executable is started with the controlled service locations
- **THEN** the build completes successfully and produces a native executable
- **AND** the executable starts successfully
- **AND** `GET /top-gods` returns status 200, content type `application/json`, and exactly Brahma (8100), Rama (845), and Hanuman (54) in descending order

### Requirement: JVM and native container support
The project SHALL support documented container-image workflows for both JVM and native application variants, and each resulting image SHALL serve the existing `GET /top-gods` behavior.

#### Scenario: JVM container serves top-gods
- **GIVEN** controlled downstream fixtures are available on an isolated container network
- **WHEN** the documented JVM container-image command builds a distinctly tagged image and that image is started with the controlled service locations
- **THEN** the application becomes ready within the documented polling window
- **AND** `GET /top-gods` returns status 200, content type `application/json`, and exactly Brahma (8100), Rama (845), and Hanuman (54) in descending order
- **AND** the verification process removes its application and fixture containers

#### Scenario: Native container serves top-gods
- **GIVEN** controlled downstream fixtures are available on an isolated container network
- **WHEN** the documented native container-image command builds a distinctly tagged image and that image is started with the controlled service locations
- **THEN** the application becomes ready within the documented polling window
- **AND** `GET /top-gods` returns status 200, content type `application/json`, and exactly Brahma (8100), Rama (845), and Hanuman (54) in descending order
- **AND** the verification process removes its application and fixture containers

### Requirement: Contributor documentation matches the verified baseline
The README SHALL document Java 25 prerequisites and the verified JVM, native executable, JVM container, and native container build and run commands.

#### Scenario: README reflects supported workflows
- **WHEN** a contributor follows the README prerequisites and commands
- **THEN** the documented Java version is Java 25
- **AND** the JVM, native executable, JVM container, and native container commands match the workflows verified for this change
