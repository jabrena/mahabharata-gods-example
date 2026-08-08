## Context

The project is a single Maven-based Spring Boot application. Its platform pins are distributed across `pom.xml`, `.sdkmanrc`, `.github/workflows/maven.yaml`, native-image plugin configuration, and `README.md`. The current baseline is Java 17, Spring Boot 3.3.1, Spring Cloud 2023.0.0, and a native-image configuration requiring GraalVM 22.3.

The application exposes `GET /top-gods`, uses Spring HTTP service proxies and `CompletableFuture` processing to query three external data sources, and ranks the three most frequent valid gods. Repository tests currently cover direct resource delegation and the ranking result using controlled test data and WireMock, but they do not exercise the HTTP route or serialized JSON contract. The WireMock integration also uses a fixed port and random delays of up to five seconds. The modernization must first establish deterministic HTTP-level compatibility evidence, then preserve that behavior while proving JVM, native-image, and container workflows on Java 25.

Stakeholders are maintainers and contributors who need one coherent toolchain, CI operators who need reproducible verification, and API consumers who require unchanged endpoint behavior. Issue #1 and its two accessible comments own the requested outcome and acceptance criteria; repository files provide current-state evidence.

## Goals / Non-Goals

**Goals:**

- Establish Java 25 and Spring Boot 4.1.0 as the consistent local, Maven, and CI baseline.
- Select a coherent set of Spring Cloud, test-library, and Maven-plugin versions or replacements supported by that baseline.
- Keep the existing `GET /top-gods` JSON contract and top-three ranking behavior.
- Retain verified Spring AOT, native executable, JVM container, and native container workflows.
- Make the documented prerequisites and commands match verified behavior.

**Non-Goals:**

- Redesign the endpoint, ranking algorithm, asynchronous processing model, or external-service boundaries.
- Add new product behavior or change the application's user-facing purpose.
- Choose dependency versions without compatibility evidence from the resolved Maven model and successful builds.
- Modify or synchronize the source GitHub issue.

## Decisions

### Use behavior-first preparation followed by the platform change

The intended change is the development platform baseline; the runtime API behavior is not intended to change. Preparation SHALL therefore establish a deterministic compatibility oracle before changing the framework baseline: an HTTP-level test for `GET /top-gods`, controlled downstream fixtures on a dynamic port, exact ordered JSON assertions, and removal of random test delays. The existing resource-delegation and ranking checks SHALL remain. Each preparatory increment SHALL pass clean Maven verification before the platform migration begins.

Combining test repair with the framework upgrade was rejected because a failing post-upgrade test would not distinguish product regression from pre-existing test nondeterminism.

### Use Spring Boot 3.5 and 4.0 as diagnostic waypoints

Implementation SHALL move through the latest available Spring Boot 3.5.x and 4.0.x maintenance releases before the final Spring Boot 4.1.0 baseline, running clean verification at each waypoint. The waypoints are review and diagnosis boundaries, not supported release baselines. They follow the Spring Boot 4 migration guidance, expose removed or deprecated APIs in smaller steps, and make rollback local to the failing increment.

A direct 3.3.1-to-4.1.0 jump was rejected because it minimizes commits at the cost of combining dependency, module, test-platform, serialization, and removed-API failures. The Boot classic starters remain a contingency only if modular test migration blocks progress; they SHALL NOT remain in the final dependency graph.

### Use Java 25 and Spring Boot 4.1.0 as fixed target constraints

The versions specified by issue #1 are requirements, not implementation options. The final Maven model SHALL use the Spring Boot 4.1.0 parent, `<java.version>25</java.version>`, and Spring Boot dependency and plugin management wherever it provides the required versions.

The final test graph SHALL use `spring-boot-starter-webflux-test` and SHALL remove redundant direct JUnit, Mockito, AssertJ, REST Assured, and managed Commons Lang version overrides. The Spring Cloud BOM and `spring-cloud-starter-contract-stub-runner` SHALL be removed because production does not use Spring Cloud discovery and the test uses only WireMock. WireMock SHALL instead use the isolated test dependency `org.wiremock:wiremock-standalone:3.13.2`. Retaining Spring Cloud 2025.1.2 was considered and is compatible with Spring Boot 4.1, but was rejected because it keeps a broad contract-testing stack for one HTTP stub extension.

### Preserve behavior through existing tests and targeted workflow verification

The compatibility oracle SHALL verify an HTTP 200 response, `application/json` content type, exactly three objects with `name` and `hitCount` members, and this exact order and data: Brahma (8100), Rama (845), and Hanuman (54). The HTTP test SHALL run without live internet services. Existing direct resource and controlled-data ranking tests SHALL remain unless their assertions are incorporated into an equally explicit replacement.

The current fixed WireMock port and random response delays SHALL be replaced by a dynamic port and deterministic responses. This improves repeatability and independence without changing application behavior. Maven verification, native compilation and startup, and runnable JVM/native container checks provide separate evidence for build and packaging outcomes; relying on compilation or artifact production alone was rejected because it would not demonstrate the acceptance criteria.

### Verify outward from the Maven baseline

Implementation SHALL proceed as four vertical slices: deterministic behavior preparation; JVM platform and toolchain migration; native executable build and runtime verification; and JVM/native container verification plus documentation reconciliation. Each slice crosses the dependencies, runtime behavior, and verification needed to produce observable maintainer value. Documentation SHALL be updated only from commands actually proven.

### Preserve the API and integration boundaries

No intentional changes are made to `GET /top-gods`, its JSON model, its top-three descending ranking, the `CompletableFuture` processing model, or the three external data-service roles. The existing `spring.cloud.discovery.client.simple.instances.*[0].uri` keys SHALL remain supported even after the Spring Cloud test dependency is removed because they are existing deployment configuration inputs.

The three explicit HTTP proxy beans SHALL remain. Compatibility adaptations SHALL use Spring Framework's replacement construction APIs, `WebClientAdapter.create(...)` and `HttpServiceProxyFactory.builderFor(...)`, without introducing HTTP Service Groups or a generic proxy factory. Changing to HTTP Service Groups was rejected because it expands the architecture beyond the modernization goal, while a generic local factory would obscure the clients' differing codec configuration.

The existing `@RequestMapping` SHALL not be opportunistically changed to `@GetMapping` during this migration. Although the documented contract is GET, restricting the accidental broader method surface is a separate compatibility decision.

### Inherit the supported native toolchain and verify runtime behavior

The project SHALL retain declarations for the Spring Boot and Native Build Tools Maven plugins but SHALL remove the custom GraalVM `requiredVersion` 22.3 constraint and redundant always-on reachability metadata configuration. The Spring Boot 4.1 parent native profile SHALL supply its managed Native Build Tools 1.1.1 configuration. Verification SHALL use a documented GraalVM 25 distribution.

Native verification SHALL include building and starting the executable against controlled downstream fixtures, then asserting the exact `GET /top-gods` response. Artifact production without startup was considered insufficient evidence that Spring AOT and runtime reachability remain functional.

### Use deterministic, reversible container verification

JVM and native images SHALL use distinct explicit tags so one build cannot overwrite the other. Each image SHALL run on an isolated container network with controlled WireMock fixtures, readiness polling, an exact endpoint assertion, captured exit status, and cleanup. The default Spring Boot buildpack may be used for this change, but the resolved builder identity SHALL be recorded with verification evidence; permanent builder pinning is a follow-up decision unless reproducibility fails without it.

No feature toggle is introduced because a runtime flag cannot switch Java, Spring Boot, or native artifacts. Rollback SHALL redeploy or revert to the last verified Java 17/Spring Boot 3.3.1 artifact; mixed old/new baselines are unsupported.

## Risks / Trade-offs

- [Major-version diagnostic complexity] Spring Boot 4 removes or reorganizes APIs and test modules -> Use 3.5.x and 4.0.x verification waypoints before the final 4.1.0 baseline.
- [Test dependency incompatibility] Explicit JUnit 5, Mockito, AssertJ, REST Assured, Spring Cloud Contract, and WireMock 2 pins conflict with the Boot 4/JUnit 6 generation -> Adopt the Boot WebFlux test starter, direct isolated WireMock 3.13.2, and framework-managed versions.
- [Framework API changes] Current HTTP proxy construction APIs are deprecated for removal -> Adapt only the three proxy constructions to their documented replacements and preserve all client interfaces and paths.
- [JSON serialization drift] Spring Boot 4 prefers Jackson 3 -> Prove the exact status, content type, member names, values, cardinality, and ordering through the HTTP characterization test.
- [Native-image incompatibility] The current GraalVM 22.3 requirement is incompatible with Java 25 -> Select a Java 25-compatible GraalVM distribution and native build plugin configuration, then run the documented native build.
- [Container build drift] Buildpack or base-image behavior may differ across the platform upgrade -> Record the resolved builder, use distinct image tags, and build, start, and exercise both images before documenting them as supported.
- [Test nondeterminism] The current fixed port and random delays can cause conflicts and variable execution time -> Use a dynamic port and deterministic fixtures before upgrading.
- [Uncontrolled integration evidence] Live Wikipedia, My JSON Server, or Archive.org behavior could make native/container checks nondeterministic -> Run all acceptance verification against controlled fixtures while retaining the production service paths and configuration keys.
- [Higher contributor baseline] Java 25 excludes contributors using Java 17 -> This is an intentional development-baseline break required by the source issue and must be prominent in the README.

## Migration Plan

1. Add the exact HTTP compatibility oracle, make WireMock deterministic, isolate its dependency, adapt the deprecated HTTP proxy construction APIs, and run clean verification without changing the platform target.
2. Move through the latest Spring Boot 3.5.x and 4.0.x maintenance waypoints with clean verification after each.
3. Establish the final Java 25/Spring Boot 4.1.0 Maven model, modular WebFlux test starter, managed dependency set, SDKMAN declaration, and JVM CI verification.
4. Inherit the Boot 4.1 native profile, build and start a native executable with GraalVM 25, and prove the exact endpoint response against controlled fixtures.
5. Build, start, exercise, and clean up distinctly tagged JVM and native container images on an isolated fixture network.
6. Update the README from the prerequisites and commands actually verified, then replay every documented workflow.

Rollback is repository-level: revert the coordinated modernization commit(s) to restore the previous Java 17/Spring Boot 3.3.1 baseline if verification cannot be completed. A mixed baseline is not a supported rollback state.

## Open Questions

- Which exact GraalVM 25 distribution identifier will be documented after native build and runtime evidence is collected?
- Does the default Boot 4.1 buildpack resolve reproducibly enough for this repository, or should its builder be pinned in a follow-up ADR?
- Where should durable native and container verification evidence live if CI automation is not added in this change?
- Is the accidental non-GET behavior of the current `@RequestMapping` a supported contract to retain indefinitely, or may it be narrowed in a separately specified change?
- Is tie ordering among gods with equal counts a contract? It is currently unspecified and SHALL not be invented by this modernization.

No conflict exists among the retrieved issue description, its two comments, and repository evidence. The remaining questions do not change the approved migration sequence or the observable acceptance outcomes.
