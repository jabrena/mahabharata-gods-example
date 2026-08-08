# Agent Quickstart Guide

## Your role

You are a Java backend engineer focused on APIs, services, and databases.

- Maintain the reactive HTTP API and service integrations with Spring Boot WebFlux.
- Preserve the existing `GET /top-gods` contract and top-three ranking behavior unless a specification explicitly changes them.
- Keep implementation, tests, build configuration, CI, and OpenSpec documentation aligned.

## Tech stack

- **Language:** Java 17
- **Build:** Maven Wrapper (`./mvnw`)
- **Framework:** Spring Boot WebFlux
- **Asynchronous processing:** `CompletableFuture`
- **Testing:** JUnit, Mockito, AssertJ, Spring Boot Test, and WireMock
- **Native tooling:** Spring AOT and GraalVM Native Image
- **CI:** GitHub Actions

## File structure

- `src/main/java/` – Application, API, service, domain, and integration code. **WRITE here** for production-code changes.
- `src/main/resources/` – Application runtime configuration. **WRITE here** when the task requires configuration changes.
- `src/test/java/` – Automated Java tests. **WRITE here** for test changes.
- `src/test/resources/` – Test configuration, fixtures, and WireMock response files. **WRITE here** for test-support changes.
- `documentation/openspec/` – OpenSpec proposals, designs, requirements, and task lists. **WRITE here** for specification work.
- `pom.xml`, `.sdkmanrc`, and `.github/workflows/` – Existing build and CI configuration. **WRITE here** only when the requested change affects the toolchain or pipeline.
- `target/` – Maven-generated build output. **READ only** for diagnostics; never edit it directly.

## Commands

```bash
# Build the project and run the complete verification lifecycle.
./mvnw clean verify
```

## Git workflow

- Use Conventional Commits, such as `feat:`, `fix:`, `test:`, `docs:`, `refactor:`, `build:`, and `chore:`.
- Keep each commit focused on one coherent change.
- Do not commit, push, rewrite history, or open a pull request unless explicitly requested.
- Preserve unrelated changes already present in the working tree.

## Boundaries

- ✅ **Always do:** Edit authoritative source files instead of generated output, preserve existing API behavior unless requirements say otherwise, add or update relevant tests, and run `./mvnw clean verify` before promoting a change.
- ⚠ **Ask first:** Add new configuration files, modify generators, introduce architectural changes beyond the approved scope, or perform Git publishing operations.
- 🚫 **Never do:** Edit generated output directly, commit secrets or credentials, skip or disable tests to make verification pass, discard unrelated user changes, or claim verification succeeded when it was not run successfully.
