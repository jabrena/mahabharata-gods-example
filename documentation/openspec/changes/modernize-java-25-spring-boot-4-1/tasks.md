## 0. Establish Deterministic Compatibility Evidence

- [ ] 0.1 Add an HTTP-level characterization test that sends `GET /top-gods` with controlled downstream inputs and asserts status 200, `application/json`, exactly three `name` and `hitCount` objects, and the ordered Brahma (8100), Rama (845), and Hanuman (54) result.
- [ ] 0.2 Replace the fixed WireMock port and random response delays with a dynamic port and deterministic fixtures while retaining the existing resource-delegation and controlled-data ranking checks.
- [ ] 0.3 Replace the Spring Cloud Contract test stack with `org.wiremock:wiremock-standalone:3.13.2`, remove the unused Spring Cloud BOM and REST Assured dependency, and preserve the existing service-location configuration keys.
- [ ] 0.4 Replace the three deprecated HTTP proxy constructions with `WebClientAdapter.create(...)` and `HttpServiceProxyFactory.builderFor(...)` without changing client interfaces, paths, codec limits, or asynchronous return types.
- [ ] 0.5 Run `./mvnw --batch-mode --no-transfer-progress clean verify` and confirm the behavior-preserving preparation is green before changing the platform baseline.

## 1. Migrate the JVM Platform Through Verified Waypoints

- [ ] 1.1 Upgrade to the latest Spring Boot 3.5.x maintenance release, resolve deprecation and dependency evidence without changing behavior, and run clean verification.
- [ ] 1.2 Upgrade to the latest Spring Boot 4.0.x maintenance release, adopt the modular `spring-boot-starter-webflux-test` dependency, remove redundant direct JUnit, Mockito, AssertJ, and managed Commons Lang version overrides, and run clean verification.
- [ ] 1.3 Configure the final Maven model for Java 25 and Spring Boot 4.1.0, resolve compatible managed dependency and plugin versions, and inspect the effective Maven model for baseline conflicts.
- [ ] 1.4 Apply only source or configuration adaptations required by Spring Boot 4.1 while preserving `GET /top-gods`, the three service roles and paths, the existing service-location keys, and the `CompletableFuture` processing model.
- [ ] 1.5 Update `.sdkmanrc` to a verified Java 25 distribution and update GitHub Actions to use Temurin 25 with `./mvnw --batch-mode --no-transfer-progress clean verify`.
- [ ] 1.6 Confirm `pom.xml`, `.sdkmanrc`, and GitHub Actions consistently declare Java 25.
- [ ] 1.7 Run `./mvnw --batch-mode --no-transfer-progress clean verify` on Java 25 and confirm the exact HTTP compatibility test, resource delegation, and ranking checks pass.

## 2. Verify the Native Executable

- [ ] 2.1 Remove the custom GraalVM 22.3 requirement and redundant always-on reachability metadata configuration, retain the Spring Boot and Native Build Tools plugin declarations, and inherit the Spring Boot 4.1 native profile with Native Build Tools 1.1.1.
- [ ] 2.2 Select and record a Java 25-compatible GraalVM distribution, run the documented native-image build command, and confirm it produces a native executable.
- [ ] 2.3 Start the native executable with controlled downstream service locations, poll until ready, and confirm `GET /top-gods` returns status 200, `application/json`, and the exact ordered Brahma, Rama, and Hanuman payload.
- [ ] 2.4 Stop the native executable and controlled fixtures and record any remaining native compatibility limitation.

## 3. Verify Distinct JVM and Native Container Images

- [ ] 3.1 Build a distinctly tagged JVM container image and record the resolved builder identity.
- [ ] 3.2 Start the JVM image and controlled WireMock fixtures on an isolated container network, poll readiness, assert the exact `GET /top-gods` response, capture the exit result, and clean up all created containers and network resources.
- [ ] 3.3 Build a distinctly tagged native container image and record the resolved builder identity without overwriting the JVM image.
- [ ] 3.4 Start the native image and controlled WireMock fixtures on an isolated container network, poll readiness, assert the exact `GET /top-gods` response, capture the exit result, and clean up all created containers and network resources.

## 4. Document and Reconcile the Verified Baseline

- [ ] 4.1 Update `README.md` with the intentional Java 25 minimum, the verified SDKMAN/GraalVM prerequisites, and the verified JVM, native executable, JVM image, and native image build and run commands.
- [ ] 4.2 Document distinct JVM/native image tags, controlled service-location arguments, readiness checks, and cleanup commands needed to replay container verification.
- [ ] 4.3 Replay every README command against the final baseline and correct any command or version that does not match successful evidence.
- [ ] 4.4 From the repository root run `./mvnw --batch-mode --no-transfer-progress clean verify`; from `documentation/` run `openspec validate --all --strict --no-interactive`; then record any remaining compatibility or reproducibility limitation before promoting the change.
