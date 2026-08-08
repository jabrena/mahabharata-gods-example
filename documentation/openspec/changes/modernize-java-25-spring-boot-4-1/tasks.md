## 1. Establish the Maven Platform Baseline

- [ ] 1.1 Configure `pom.xml` for Java 25 and Spring Boot 4.1.0.
- [ ] 1.2 Resolve the effective Maven model and select compatible Spring Cloud, test-library, and Maven-plugin versions or replacements.
- [ ] 1.3 Apply only the source or configuration adaptations required for Spring Boot 4.1.0 compatibility while preserving the endpoint and integration boundaries.

## 2. Preserve and Verify Application Behavior

- [ ] 2.1 Retain the existing resource-level checks for a successful `GET /top-gods` response and service delegation.
- [ ] 2.2 Retain the controlled-data ranking checks for exactly three descending results: Brahma (8100), Rama (845), and Hanuman (54).
- [ ] 2.3 Add any missing automated coverage needed to prove the JSON endpoint contract without relying on uncontrolled external services.
- [ ] 2.4 Run `./mvnw --batch-mode --no-transfer-progress clean verify` on Java 25 and record a successful result.

## 3. Align Local and CI Toolchains

- [ ] 3.1 Update `.sdkmanrc` to select a Java 25 distribution.
- [ ] 3.2 Update GitHub Actions to use Java 25 and the required clean Maven verification command.
- [ ] 3.3 Confirm Maven, SDKMAN, and GitHub Actions consistently declare Java 25.

## 4. Verify Native and Container Workflows

- [ ] 4.1 Select and configure a Java 25-compatible GraalVM distribution and native-image Maven tooling.
- [ ] 4.2 Run the native-image build command and confirm it produces a runnable native executable.
- [ ] 4.3 Build and start the JVM container image, then confirm `GET /top-gods` returns the top-three result.
- [ ] 4.4 Build and start the native container image, then confirm `GET /top-gods` returns the top-three result.

## 5. Document and Reconcile the Verified Baseline

- [ ] 5.1 Update `README.md` prerequisites for Java 25 and the selected compatible GraalVM distribution.
- [ ] 5.2 Document the verified JVM, native executable, JVM container, and native container build and run commands.
- [ ] 5.3 Cross-check every README command and version against the successful verification evidence and record any remaining compatibility limitation.
