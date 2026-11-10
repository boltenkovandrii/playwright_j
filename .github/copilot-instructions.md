# Copilot instructions

## Project purpose

This repository contains Java Playwright UI tests for the PrestaShop demo. The suite validates core storefront behavior and emphasizes reliable local and CI execution, maintainable Page Objects, reproducible Docker-based application setup, and actionable Allure diagnostics.

Do not introduce browser-driver or remote-browser infrastructure that is not supported by this Playwright-only project.

## Build and test commands

Use the Gradle Wrapper and Java 25. On Windows, use `.\gradlew.bat`; on macOS and Linux, use `./gradlew`.

* Run the default regression suite: `.\gradlew.bat clean test`
* Run static analysis: `.\gradlew.bat qualityCheck`
* Run all storefront tests for selected browsers and profiles: `.\gradlew.bat test --tests "com.prestashop.test.tests.*" -Dbrowsers="chromium,webkit" -Dprofiles="desktop_1920x1200,mobile"`
* Run one test class: `.\gradlew.bat test --tests "com.prestashop.test.tests.HomePageTest"`
* Run headed tests: append `-Dheadless=false`
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

* Test scenarios belong in `src/test/java/com/prestashop/test/tests`.
* Page Objects belong in `src/test/java/com/prestashop/test/pages`.
* Shared configuration and browser/profile selection belong in `base`; lifecycle, retry, and artifact code belongs in `runner`. Reuse the existing lifecycle rather than creating parallel Playwright setup.
* Reusable UI fragments belong in `src/test/java/com/prestashop/test/components`.
* Runtime defaults belong in `src/test/resources/config.properties`.
* Business-level scenario documentation belongs in `docs/scenarios`.
* Use Page Objects and existing components for browser interactions. Keep selectors and assertions localized to the appropriate page or component.
* Keep browser contexts and Playwright lifecycle thread-safe. Preserve retry behavior and Allure failure diagnostics when changing test execution.

## Browser and application configuration

* Supported browsers are `chromium`, `firefox`, and `webkit`.
* Supported profiles are `desktop_2560x1600`, `desktop_1920x1200`, `mobile_landscape`, `mobile`, `tablet_landscape`, and `tablet`.
* Firefox is restricted to desktop profiles because Playwright does not support Firefox mobile-device emulation.
* The default local PrestaShop URL is `http://localhost:8090`; Docker Compose configuration is in `docker/docker-compose-prestashop.yml`.
* Do not add browser driver binaries, machine-specific paths, or unsupported remote-browser configuration.

## Reporting, retries, and CI

* Failed attempts retain and attach screenshots, videos, and traces to Allure. Successful first attempts discard these artifacts; successful retries retain video.
* `-DallureScreenshots=true` attaches screenshots for all attempts. `-DretryAttempts=<count>` overrides the configured retry count.
* The generated Allure report is at `build/reports/allure-report/allureReport/index.html`.
* `.github/workflows/tests-run.yml` starts the PrestaShop container, runs Checkstyle and PMD, installs Linux WebKit dependencies, executes tests, and uploads the Allure report with `if: always()`.
* Preserve the workflow's failure semantics: report upload must not hide a failed quality check or test run, and teardown/report collection should remain available after failures.

## Change guidelines

* Prefer focused changes that match the existing Java, Gradle, JUnit, Playwright, and documentation conventions.
* Update the README when commands, runtime configuration, Docker startup, reporting paths, browser/profile support, or CI behavior changes.
* Update the matching `docs/scenarios` document when a scenario's user-facing behavior changes materially.
* Use stable dependency versions and avoid unrelated dependency upgrades or new tools.
* Do not expand the suite merely to increase the number of technologies or test cases.
* Never claim a build, quality check, test run, Docker operation, or report publication passed unless it was run and verified.
