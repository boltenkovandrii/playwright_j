# PrestaShop Playwright Java tests

Java UI tests for the PrestaShop demo storefront, built with Playwright, JUnit Jupiter, Gradle, Docker Compose, and Allure.

The suite exercises core storefront journeys such as navigation, catalog browsing, product details, cart, checkout, account, search, and localization. It is intended as a maintainable browser-automation project rather than a complete test strategy for PrestaShop.

## Technology stack

* Java 25
* Gradle Wrapper
* JUnit Jupiter
* Playwright for Java
* Docker Compose and the PrestaShop demo image
* Allure Report
* Checkstyle and PMD
* GitHub Actions

## Project structure

```text
.
├── .github/
│   ├── workflows/tests-run.yml
│   └── copilot-instructions.md
├── docker/
│   └── docker-compose-prestashop.yml
├── docs/scenarios/storefront/
├── src/test/
│   ├── java/com/prestashop/test/
│   │   ├── base/          # shared configuration and parallel execution
│   │   ├── runner/        # Playwright options, retries, and artifact diagnostics
│   │   ├── components/    # reusable storefront UI components
│   │   ├── pages/         # Page Objects
│   │   └── tests/         # storefront test scenarios
│   └── resources/
│       ├── config.properties
│       ├── checkstyle.xml
│       └── pmd.ruleset.xml
├── build.gradle
├── gradlew
├── allurerc.json
└── gradlew.bat
```

## Prerequisites

* JDK 25.
* Docker Desktop or a compatible Docker Engine with Docker Compose.
* Git.

Gradle is provided through the wrapper.

## Run the application and tests

Start the local PrestaShop before running tests:

```powershell
docker compose -f docker\docker-compose-prestashop.yml up -d
```

The storefront is available at `http://localhost:8090`. Run the default regression suite:

```powershell
.\gradlew.bat clean test
```

On macOS or Linux, use `./gradlew clean test`. Stop the local application when it is no longer needed:

```powershell
docker compose -f docker\docker-compose-prestashop.yml down
```

### Select a profile

Tests extend `BaseTest`, which configures Playwright's JUnit integration with `PlaywrightOptions`. A profile selects both the browser and device/context configuration, so a local Gradle test run accepts one profile at a time. The default profile is `Desktop Chrome`.

```powershell
.\gradlew.bat test --tests "com.prestashop.test.tests.*" -Dprofile="iPhone 13"
```

Available profile names are defined by `availableProfiles` in `src/test/resources/config.properties`:

* `Desktop Chrome`
* `Desktop Firefox`
* `Desktop Safari`
* `iPhone 13`
* `iPhone 13 landscape`
* `iPad Mini`
* `iPad Mini landscape`
* `Desktop Chrome 2560x1600`
* `Desktop Firefox 2560x1600`
* `iPad Pro 11 Chromium`
* `iPad Pro 11 landscape Chromium`

Other useful options:

```powershell
# Run a single test class with a desktop Firefox profile in headed mode.
.\gradlew.bat test --tests "com.prestashop.test.tests.HomePageTest" `
  -Dprofile="Desktop Firefox" `
  -Dheadless=false

# Override the storefront URL.
.\gradlew.bat test -DbaseUrlPrestaShop="http://localhost:8090"

# Set the number of parallel test threads.
.\gradlew.bat test -Dthreads=2

# Disable reruns.
.\gradlew.bat test -DretryAttempts=0

# Retain and attach screenshots for every attempt.
.\gradlew.bat test -DallureScreenshots=true
```

The `regression` tag is included by default and the `WIP` tag is excluded. Select a different tag with `-DincludeTags=<tag>`.

Runtime defaults are in `src/test/resources/config.properties`. System properties override those defaults.

## Quality checks and reports

Run the configured static analysis checks:

```powershell
.\gradlew.bat qualityCheck
```

Each test run finalizes the Allure report. Open the generated report at:

```text
build/reports/allure-report/allureReport/index.html
```

Failed attempts retain and attach a full-page screenshot, Playwright trace, and video. Successful first attempts discard these artifacts; successful retry attempts retain the video. Use `-DallureScreenshots=true` to attach screenshots for all attempts.

## CI

`.github/workflows/tests-run.yml` runs on PRs to `main`, pushes to `main` and `feature/**`, on a monthly schedule, and on manual dispatch. It has four jobs:

1. **Resolve test parameters** validates the selected profile names and resolves tags and thread count.
2. **Quality checks** runs Checkstyle and PMD once.
3. **Tests** runs one matrix job per selected profile, installing Playwright browsers and dependencies and starting PrestaShop for each job. Each invocation uses a single `-Dprofile`.
4. **Generate combined Allure report** downloads and merges the per-profile Allure results, generates one report, and uploads it as an artifact.

Manual workflow dispatch accepts comma-separated profile names, the test tag, and thread count. Pushes to `main` and `feature/**` use `Desktop Chrome`; pull requests and scheduled runs cover all profiles from `config.properties`. The combined report is generated when the test jobs succeed or fail, and the per-profile results and report are uploaded as artifacts.

## Scenario documentation

Business-level descriptions of the storefront scenarios are in `docs/scenarios/storefront/`. Keep these documents aligned when adding or materially changing a test scenario.
