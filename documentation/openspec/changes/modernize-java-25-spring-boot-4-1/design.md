## Context

The project is a single Maven-based Spring Boot application. Its platform pins are distributed across `pom.xml`, `.sdkmanrc`, `.github/workflows/maven.yaml`, native-image plugin configuration, and `README.md`. The current baseline is Java 17, Spring Boot 3.3.1, Spring Cloud 2023.0.0, and a native-image configuration requiring GraalVM 22.3.

The application exposes `GET /top-gods`, uses Spring HTTP service proxies and `CompletableFuture` processing to query three external data sources, and ranks the three most frequent valid gods. Repository tests currently cover the HTTP resource delegation and the ranking result using controlled test data and WireMock. The modernization must preserve this behavior while proving JVM, native-image, and container workflows on Java 25.

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

### Treat the upgrade as one coordinated compatibility migration

Java, Spring Boot, managed dependencies, Maven plugins, CI, SDKMAN, tests, native tooling, containers, and documentation SHALL move together because a partial migration would not produce a usable platform baseline. Splitting work by file or technical layer was considered, but rejected because none of those slices independently satisfies the issue outcome.

### Use Java 25 and Spring Boot 4.1.0 as fixed target constraints

The versions specified by issue #1 are requirements, not implementation options. Dependency and plugin versions beneath that baseline SHALL be selected from combinations compatible with Spring Boot 4.1.0 and Java 25, favoring framework dependency management over redundant direct test-version overrides where compatibility requires it. Pinning every secondary version in this design was considered, but deferred because the issue explicitly identifies compatibility resolution as unknown and requires build evidence.

### Preserve behavior through existing tests and targeted workflow verification

The existing resource and ranking tests SHALL remain the behavioral regression baseline. The modernization may adapt test infrastructure for framework compatibility, but SHALL retain checks for successful endpoint delegation and the three ranked results. Maven verification, native compilation, and runnable JVM/native container checks provide separate evidence for build and packaging outcomes; relying on compilation alone was rejected because it would not demonstrate the acceptance criteria.

### Verify outward from the Maven baseline

Implementation SHALL first align and verify the Java/Maven dependency graph, then local and CI toolchain declarations, then native-image support, then JVM/native containers, and finally documentation against the commands actually proven. This ordering localizes compatibility failures and prevents documentation from describing unverified commands. Updating all surfaces before establishing a working Maven baseline was considered, but would make failures harder to diagnose.

### Preserve the API and integration boundaries

No intentional changes are made to `GET /top-gods`, its JSON model, its top-three descending ranking, or the three external data-service roles. Source or configuration adaptations required by Spring Framework 7 or Spring Boot 4.1 are permitted only when they preserve those boundaries and behavior.

## Risks / Trade-offs

- [Spring ecosystem incompatibility] Spring Cloud 2023.0.0 or explicit test dependencies may not work with Spring Boot 4.1.0 -> Resolve the effective Maven model, use compatible managed releases or replacements, and prove the result with `clean verify`.
- [Framework API changes] Spring Boot 4.1.0 may require source or configuration adaptations -> Keep adaptations narrowly scoped and retain endpoint and ranking regression checks.
- [Native-image incompatibility] The current GraalVM 22.3 requirement is incompatible with Java 25 -> Select a Java 25-compatible GraalVM distribution and native build plugin configuration, then run the documented native build.
- [Container build drift] Buildpack or base-image behavior may differ across the platform upgrade -> Build, start, and exercise both JVM and native images before documenting them as supported.
- [Insufficient regression evidence] Existing tests may not fully exercise the HTTP-level response contract or packaging modes -> Retain current assertions and add only the coverage needed to prove the OpenSpec scenarios.
- [Higher contributor baseline] Java 25 excludes contributors using Java 17 -> This is an intentional development-baseline break required by the source issue and must be prominent in the README.

## Migration Plan

1. Resolve the Java 25/Spring Boot 4.1.0 Maven model and establish compatible dependency and plugin configuration.
2. Run the complete Maven verification suite on Java 25 and adapt only compatibility-sensitive source or test code while preserving behavior.
3. Align `.sdkmanrc` and GitHub Actions with Java 25 and confirm CI uses the same Maven verification command.
4. Select and verify a Java 25-compatible GraalVM/native-image toolchain.
5. Build and run JVM and native container images and exercise `GET /top-gods` for each.
6. Update the README from the commands and prerequisites that were actually verified.

Rollback is repository-level: revert the coordinated modernization commit(s) to restore the previous Java 17/Spring Boot 3.3.1 baseline if verification cannot be completed. A mixed baseline is not a supported rollback state.

## Open Questions

- Which Spring Cloud release or replacement is compatible with Spring Boot 4.1.0 and the existing simple-discovery configuration?
- Which explicit test dependency versions can be removed in favor of Spring Boot dependency management, and which must remain pinned?
- Which Java 25-compatible GraalVM distribution and native Maven plugin configuration successfully build this project?
- Do Spring Framework 7 API changes require adaptations to the HTTP service proxy construction?
- What additional automated or scripted checks are needed to prove the HTTP-level and container scenarios without depending on uncontrolled external services?

These questions are implementation-time compatibility investigations. No conflict exists among the retrieved issue description, its two comments, and repository evidence.
