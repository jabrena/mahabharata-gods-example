## Why

The repository's Java 17 and Spring Boot 3.3.1 baseline is inconsistent with the current target platform and leaves local development, CI, native-image, container, and documentation workflows increasingly difficult to maintain together. Modernizing these surfaces as one coordinated change establishes a reproducible Java 25 and Spring Boot 4.1.0 baseline while preserving the application's existing purpose and API behavior.

## What Changes

- **BREAKING (development baseline)**: Require Java 25 for Maven compilation, local SDKMAN use, and CI builds.
- Upgrade the Maven project to Spring Boot 4.1.0 and align dependencies and build plugins with that baseline.
- Preserve the `GET /top-gods` JSON response and top-three ranking behavior, backed by the existing automated tests.
- Retain Spring AOT and native-image support using a Java 25-compatible GraalVM distribution.
- Verify both JVM and native container-image workflows continue to serve `GET /top-gods`.
- Update contributor documentation with accurate Java 25 prerequisites and JVM, native, and container build and run commands.

## Capabilities

### New Capabilities

- `development-platform-baseline`: Defines the supported Java and Spring Boot baseline, reproducible verification, behavioral compatibility, native-image and container support, and contributor documentation requirements.

### Modified Capabilities

None. The OpenSpec project has no existing capability specifications, and this change preserves the user-facing API contract.

## Impact

- Build configuration: `pom.xml`, Maven dependency management, and Maven plugins.
- Developer and CI toolchains: `.sdkmanrc` and `.github/workflows/maven.yaml`.
- Verification: existing endpoint and ranking tests plus JVM, native-image, and container workflow checks.
- Documentation: `README.md` prerequisites and supported build/run commands.
- Runtime API: no intended behavior or contract change to `GET /top-gods` or its external service integrations.

## Source and Derivation

- Source issue: https://github.com/jabrena/mahabharata-gods-example/issues/1
- Accessible snapshot retrieved: 2026-08-08T15:20:55Z
- Accessible comments: 2 retrieved, matching the provider-reported total of 2.
- Derivation direction: GitHub issue #1 description and comments -> this OpenSpec change. No synchronization back to the issue is authorized or implied.
- Authority: the issue owns the requested outcome, scope, and acceptance criteria; the repository owns current implementation evidence; this OpenSpec change derives implementable requirements without modifying either source.

## Scope Assessment

This is one reviewable change. The Java, Spring Boot, dependency, plugin, CI, native-image, container, test, and documentation updates jointly deliver one platform-baseline outcome and do not provide independent value when separated by technical layer.

## Unresolved Questions and Compatibility Assumptions

- The exact Spring Cloud, test-library, and Maven-plugin versions or replacements compatible with Spring Boot 4.1.0 remain an implementation-time compatibility decision.
- The Java 25-compatible GraalVM distribution and any required native plugin configuration remain to be established by build evidence.
- JVM and native container commands are assumed to remain supportable, but their exact post-upgrade form must be verified and documented.
- Existing tests are the required starting point for compatibility verification; implementation must determine whether additional coverage is needed to establish the stated behavior and container outcomes.
