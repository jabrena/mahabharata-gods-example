## ADDED Requirements

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
- **WHEN** a client sends `GET /top-gods` to the modernized application with the existing controlled test inputs
- **THEN** the response is successful and contains exactly three JSON god results
- **AND** the results preserve the existing descending ranking of Brahma with 8100 hits, Rama with 845 hits, and Hanuman with 54 hits

### Requirement: Native executable support
The project SHALL support Spring AOT and native executable compilation with a Java 25-compatible GraalVM distribution.

#### Scenario: Documented native build succeeds
- **WHEN** the documented native-image build command is run using the documented Java 25-compatible GraalVM distribution
- **THEN** the command completes successfully and produces a native executable

### Requirement: JVM and native container support
The project SHALL support documented container-image workflows for both JVM and native application variants, and each resulting image SHALL serve the existing `GET /top-gods` behavior.

#### Scenario: JVM container serves top-gods
- **WHEN** the documented JVM container-image command builds an image and that image is started
- **THEN** a `GET /top-gods` request returns the top three ranked gods

#### Scenario: Native container serves top-gods
- **WHEN** the documented native container-image command builds an image and that image is started
- **THEN** a `GET /top-gods` request returns the top three ranked gods

### Requirement: Contributor documentation matches the verified baseline
The README SHALL document Java 25 prerequisites and the verified JVM, native executable, JVM container, and native container build and run commands.

#### Scenario: README reflects supported workflows
- **WHEN** a contributor follows the README prerequisites and commands
- **THEN** the documented Java version is Java 25
- **AND** the JVM, native executable, JVM container, and native container commands match the workflows verified for this change
