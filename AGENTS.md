# Copilot instructions

## Project purpose

This repository contains Java Playwright UI tests for the PrestaShop demo. The suite validates core behavior and emphasizes reliable local and CI execution, maintainable Page Objects, reproducible Docker-based application setup, and actionable Allure diagnostics.

Do not introduce browser-driver or remote-browser infrastructure that is not supported by this Playwright-only project.

## Build and test commands

Use the Gradle Wrapper and Java 25. On Windows, use `.\gradlew.bat`; on macOS and Linux, use `./gradlew`.

* Run the default regression suite: `.\gradlew.bat clean test`
* Run static analysis: `.\gradlew.bat qualityCheck`
* Run all tests for one profile: `.\gradlew.bat test --tests "com.prestashop.test.tests.*" -Dprofile="iPhone 13"`
* Run one test class: `.\gradlew.bat test --tests "com.prestashop.test.tests.HomePageTest"`
* Run headed tests: append `-Dheadless=false`
* Set parallel test threads: append `-Dthreads=<count>`
* Run against another storefront URL: append `-DbaseUrlPrestaShop=<url>`

Start the application under test before local test runs:

```powershell
docker compose -f docker\docker-compose-prestashop.yml up -d
```

Stop it with:

```powershell
docker compose -f docker\docker-compose-prestashop.yml down
```

The default test tag is `regression`; `WIP` is excluded. The `includeTags` system property selects another tag. `config.properties` provides runtime defaults, and system properties take precedence.

Do not add handling that turns unexpected test, quality-check, or workflow failures into successes.

## Framework conventions

* Test scenarios belong in `src/test/java/com/prestashop/test/tests` and extend `base.BaseTest`. `BaseTest` configures Playwright's JUnit integration with `@UsePlaywright(PlaywrightOptions.class)`; do not reintroduce the removed custom test runner.
* Page Objects belong in `src/test/java/com/prestashop/test/pages`.
* Shared configuration and parallel execution belong in `base`; Playwright profile options, retry tracking, and artifact diagnostics belong in `runner`. Reuse the existing Playwright JUnit integration and lifecycle rather than creating parallel setup.
* Reusable UI fragments belong in `src/test/java/com/prestashop/test/components`.
* Runtime defaults belong in `src/test/resources/config.properties`.
* Business-level scenario documentation belongs in `docs/scenarios`.
* Use Page Objects and existing components for browser interactions. Keep selectors and assertions localized to the appropriate page or component.
* Keep browser contexts and Playwright lifecycle thread-safe. Preserve retry behavior and Allure failure diagnostics when changing test execution.

## Browser and application configuration

* `src/test/resources/config.properties` defines available profiles via `availableProfiles`. Each profile combines a browser with a device or context configuration; use the exact profile name with the singular `-Dprofile=<name>` property.
* Available profiles are `Desktop Chrome`, `Desktop Firefox`, `Desktop Safari`, `iPhone 13`, `iPhone 13 landscape`, `iPad Mini`, `iPad Mini landscape`, `Desktop Chrome 2560x1600`, `Desktop Firefox 2560x1600`, `iPad Pro 11 Chromium`, and `iPad Pro 11 landscape Chromium`.
* A local Gradle test run uses one profile. CI runs multiple profiles as separate matrix jobs.
* The default local PrestaShop URL is `http://localhost:8090`; Docker Compose configuration is in `docker/docker-compose-prestashop.yml`.
* Do not add browser driver binaries, machine-specific paths, or unsupported remote-browser configuration.

## Reporting, retries, and CI

* Failed attempts retain and attach screenshots, videos, and traces to Allure. Successful first attempts discard these artifacts; successful retries retain video.
* `-DallureScreenshots=true` attaches screenshots for all attempts. `-DretryAttempts=<count>` overrides the configured retry count.
* The generated Allure report is at `build/reports/allure-report/allureReport/index.html`.
* `.github/workflows/tests-run.yml` has four jobs: parameter resolution, quality checks, one test job per selected profile, and combined Allure report generation. Each test job installs Playwright browsers and dependencies, starts and stops PrestaShop, and uploads its raw Allure results. The report job merges results from the matrix and uploads a combined report when tests succeed or fail.
* Pushes to `main` and `feature/**` use `Desktop Chrome`; pull requests to `main` and scheduled runs use all configured profiles. Manual dispatch accepts profile names, tags, and thread count.
* Preserve the workflow's failure semantics: PrestaShop teardown and per-profile result uploads run with `if: always()`. The combined report is attempted when parameter resolution succeeds and test jobs succeed or fail; its artifact upload also runs with `if: always()`. Do not hide a failed quality check or test run.

## Change guidelines

* Prefer focused changes that match the existing Java, Gradle, JUnit, Playwright, and documentation conventions.
* Update the README when commands, runtime configuration, Docker startup, reporting paths, browser/profile support, or CI behavior changes.
* Update the matching `docs/scenarios` document when a scenario's user-facing behavior changes materially.
* Use stable dependency versions and avoid unrelated dependency upgrades or new tools.
* Do not expand the suite merely to increase the number of technologies or test cases.
* Never claim a build, quality check, test run, Docker operation, or report publication passed unless it was run and verified.

## Git
- Add newly generated .java, .md, .yml, and .properties files to the repository if appropriate (don't add temporary files), but never commit or push them.
- Never commit or push unless explicitly requested.
- Never change Git history or force-push to a shared branch unless explicitly requested.

## Permissions
- Never try to avoid configured security or permission checks, or to bypass the configured approval process for Git commands, shell commands, or other operations.
- If a command is blocked, ask for approval or guidance rather than trying to bypass the block.
- If a command is blocked, show exactly what you tried to run and the error message, and ask for guidance on how to proceed.

## Test execution
- Ask for approval before executing commands.
- Report the exact command and its result.
- Do not claim that tests passed unless they actually ran.