# Selenium Pipeline — Docker, Selenium Grid and CI

A Java-based Selenium test project focused on **browser automation infrastructure, Docker images, Selenium Grid, CI pipelines, and Allure reporting**.

The project demonstrates how to run browser tests locally and in a containerized Selenium Grid, build and publish custom Selenium images, execute quality checks, and preserve test reports and diagnostic logs.

## Project goals

The main goal is to demonstrate the infrastructure around automated UI testing rather than to provide a large, production-level test suite.

The project uses Wikipedia as a simple application under test (AUT). The test suite is intentionally small and demonstrates the framework and execution pipeline. It is not intended to provide comprehensive coverage of Wikipedia or to demonstrate a full test strategy for a complex web application.

## Technology stack

* **Java 17**
* **Gradle** with the Gradle Wrapper
* **JUnit Jupiter 6**
* **Selenium WebDriver 4**
* **Selenium Grid**
* **Docker and Docker Compose**
* **Allure Report**
* **GitHub Actions**
* **GitHub Container Registry (GHCR)**
* **Checkstyle and PMD**

## Main capabilities

* Run UI tests locally with Chrome, Firefox, or Microsoft Edge.
* Run tests remotely using Selenium Grid.
* Build custom Docker images for the Grid hub and browser nodes.
* Publish container images to GHCR.
* Select tests by JUnit tags.
* Configure parallel test execution.
* Generate Allure reports.
* Collect browser-container logs and download artifacts in CI.
* Run static analysis with Checkstyle and PMD.
* Retain historical Allure reports on GitHub Pages.
* Clean up old container-image versions and reports with scheduled workflows.

## Project structure

```text
.
├── .github/
│   ├── workflows/
│   │   ├── tests-run.yml
│   │   ├── build-images.yml
│   │   ├── clean-images.yml
│   │   └── clean-old-reports.yml
│   └── copilot-instructions.md
├── dockerfiles/
│   ├── Dockerfile_hub
│   ├── Dockerfile_chrome
│   ├── Dockerfile_firefox
│   └── Dockerfile_edge
├── src/
│   └── test/
│       ├── java/com/andrii/test/
│       │   ├── base/
│       │   ├── pages/
│       │   └── tests/
│       └── resources/
│           ├── config.properties
│           ├── checkstyle.xml
│           └── pmd.ruleset.xml
├── docker-compose-gh.yml
├── build.gradle
└── gradlew
```

The main Java packages are:

* `base` — configuration, WebDriver creation, test lifecycle, event handling, and parallel execution.
* `pages` — Page Objects and reusable page components.
* `tests` — UI test scenarios.

## Prerequisites

For local browser tests:

* JDK 17 or a compatible JDK installation.
* A supported browser installed locally.
* Git.

Gradle is provided by the project wrapper, so a separate Gradle installation is not required.

For Docker-based execution, Docker and Docker Compose are also required.

**Browser drivers:** Selenium Manager handles local driver resolution. Browser driver binaries are not maintained in the repository. For remote execution, Selenium Grid browser-node containers provide the browser and driver environment.

## Run tests locally

Clone the repository and open its directory:

```bash
git clone https://github.com/boltenkovandrii/selenium_pipeline_cp.git
cd selenium_pipeline_cp
```

On Windows, use `.\gradlew.bat` instead of `./gradlew` if required by your shell.

Run the default test suite:

```bash
./gradlew clean test
```

The default configuration is read from `src/test/resources/config.properties`. It selects the browser, execution mode, and default thread count.

### Select a browser and test tag

For example, run tests tagged `current` using Microsoft Edge with two parallel workers:

```bash
./gradlew clean test -DincludeTags=current -Dbrowser=edge -Dthreads=2
```

Supported browser values are `chrome`, `firefox`, and `edge`.

By default, the test task includes the `regression` tag and excludes tests tagged `WIP`. Supplying `-DincludeTags` selects a different tag.

To run a specific test class:

```bash
./gradlew test --tests "com.andrii.test.tests.OpenPageTest"
```

Replace the example class with a test class that exists in the repository.

## Code quality checks

Run Checkstyle and PMD:

```bash
./gradlew qualityCheck
```

These checks are also run in the GitHub Actions test workflow before test execution.

## Docker images and Selenium Grid

The repository contains custom Dockerfiles for the Selenium Grid hub and Chrome, Firefox, and Edge browser nodes.

The `Build and push Docker images` workflow builds the selected images and publishes them to GHCR. It supports manual execution with configurable base-image versions and scheduled image maintenance.

The `Run tests with Selenium Grid` workflow:

1. Pulls the configured hub and browser images from GHCR.
2. Starts Selenium Grid and scales the selected browser nodes.
3. Runs the selected test suite against the remote browser.
4. Collects container logs and generated artifacts.
5. Publishes the Allure report to GitHub Pages.

The test workflow supports manual configuration of the browser, test tag, thread count, hub-image tag, and browser-image tag.

The workflow uses `latest` image tags by default. Specific tags can be selected for controlled runs.

## GitHub Actions workflows

| Workflow                | Purpose                                                                                                  |
| ----------------------- | -------------------------------------------------------------------------------------------------------- |
| `tests-run.yml`         | Runs static analysis and UI tests, starts Selenium Grid, and publishes reports and diagnostic artifacts. |
| `build-images.yml`      | Builds and publishes the hub and browser-node images to GHCR.                                            |
| `clean-images.yml`      | Removes older container-image versions according to the configured retention rules.                      |
| `clean-old-reports.yml` | Removes older Allure report directories from the `gh-pages` branch.                                      |

The test workflow runs on pushes to `main` and `feature/**`, on a weekly schedule, and on manual requests. The image-building and cleanup workflows have their own manual and scheduled triggers.

## Allure reports and diagnostics

The Gradle build generates an Allure report at:

```text
build/reports/allure-report/allureReport/
```

Open `index.html` to view a locally generated report.

In CI, the workflow publishes a report to GitHub Pages and uploads diagnostic artifacts, including browser-container logs. Historical reports are stored under date- and branch-specific directories.

The published report link is included in the GitHub Actions job summary.

## Scope and limitations

This repository focuses on the infrastructure needed to execute and report browser tests. Its Wikipedia tests are deliberately limited in scope.

The project does not aim to demonstrate:

* Comprehensive functional coverage of a complex application.
* A full production application test strategy.

The emphasis is on repeatable test execution, browser infrastructure, Docker image management, CI orchestration, and test reporting.
