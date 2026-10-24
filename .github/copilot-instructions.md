# Copilot instructions

## Project purpose

This repository demonstrates Selenium UI test execution infrastructure using Java, JUnit Jupiter, Gradle, Docker, Selenium Grid, GitHub Actions, and Allure Report.

The application under test is Wikipedia. The test suite is intentionally small. Prioritize reliable test execution, maintainable framework code, Docker configuration, CI behavior, and diagnostics over adding many test scenarios or technologies.

## Build and test commands

Use the Gradle Wrapper.

* Run the default test suite: `./gradlew clean test`
* Run static analysis: `./gradlew qualityCheck`
* Run tests with a selected tag and browser: `./gradlew clean test -DincludeTags=current -Dbrowser=edge -Dthreads=2`
* Run one test class: `./gradlew test --tests "com.andrii.test.tests.OpenPageTest"`

On Windows, use `.\gradlew.bat` when appropriate.

The default test tag is `regression`. Tests tagged `WIP` are excluded by the Gradle test configuration. The `includeTags` system property overrides the default included tag.

Do not introduce special handling that allows unexpected test failures to pass silently. Preserve normal Gradle test failure behavior.

## Framework conventions

* Test scenarios belong in `src/test/java/com/andrii/test/tests`.
* Page Objects belong in `src/test/java/com/andrii/test/pages`.
* Shared test infrastructure belongs in `src/test/java/com/andrii/test/base`.
* Runtime defaults are defined in `src/test/resources/config.properties`.
* Use Page Objects for browser interactions where appropriate.
* Keep browser selection and local-versus-Grid execution configurable.
* Preserve thread safety when changing WebDriver lifecycle or parallel execution.
* Keep Allure reporting and failure diagnostics working when changing test lifecycle code.

## Browser and Grid management

* Local browser sessions use Selenium WebDriver constructors and Selenium Manager.
* Do not add browser driver binaries or driver-update scripts to the repository.
* Do not introduce machine-specific browser or driver paths into shared configuration.
* Remote sessions use `RemoteWebDriver` and Selenium Grid browser-node containers.
* Dockerfiles are in `dockerfiles/`; the GitHub Actions Grid configuration is in `docker-compose-gh.yml`.

## CI and reporting

* `.github/workflows/tests-run.yml` runs Checkstyle and PMD before UI tests, starts Selenium Grid, collects container logs, publishes Allure reports, and uploads diagnostic artifacts.
* `.github/workflows/build-images.yml` builds and publishes custom Grid and browser images to GHCR.
* The image and report cleanup workflows manage historical resources.

When modifying workflows, preserve the intended failure behavior. A test or quality-check failure must fail the relevant workflow step. Report and log collection should still run when possible.

Do not assume that a successful report-upload step means the tests passed; check the test and quality-check steps separately.

## Dependencies and configuration

* Java toolchain: 17.
* Build configuration: `build.gradle`.
* Browser and Grid defaults: `src/test/resources/config.properties`.
* Checkstyle rules: `src/test/resources/checkstyle.xml`.
* PMD rules: `src/test/resources/pmd.ruleset.xml`.

Use stable dependency versions. Avoid unrelated dependency upgrades and new tools unless they provide a clear benefit.

## Change guidelines

* Prefer small, focused changes.
* Do not add technologies solely to increase the technology list.
* Do not expand the Wikipedia test suite just to make it look larger.
* Update the README when commands, workflows, configuration, or report paths change.
* Never claim a build or test passed unless it was actually run and verified.
