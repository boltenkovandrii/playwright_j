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
│   │   ├── base/          # configuration and browser/profile settings
│   │   ├── runner/        # test lifecycle, retries, and diagnostics
│   │   ├── components/    # reusable storefront UI components
│   │   ├── pages/         # Page Objects
│   │   └── tests/         # storefront test scenarios
│   └── resources/
│       ├── config.properties
│       ├── checkstyle.xml
│       └── pmd.ruleset.xml
├── build.gradle
├── gradlew
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

### Select browsers and profiles

The default execution uses Chromium and the `desktop_1920x1200` profile. Supply comma-separated values to run the valid browser/profile combinations:

```powershell
.\gradlew.bat test --tests "com.prestashop.test.tests.*" `
  -Dbrowsers="chromium,webkit" `
  -Dprofiles="desktop_1920x1200,mobile,tablet_landscape"
```

Supported browsers are `chromium`, `firefox`, and `webkit`. Supported profiles are:

* `desktop_2560x1600`
* `desktop_1920x1200`
* `mobile_landscape`
* `mobile`
* `tablet_landscape`
* `tablet`

Firefox supports desktop profiles only because Playwright does not support Firefox mobile-device emulation.

Other useful options:

```powershell
# Run Firefox in headed mode on both desktop profiles.
.\gradlew.bat test --tests "com.prestashop.test.tests.*" `
  -Dbrowsers="firefox" `
  -Dprofiles="desktop_1920x1200,desktop_2560x1600" `
  -Dheadless=false

# Override the storefront URL.
.\gradlew.bat test -DbaseUrlPrestaShop="http://localhost:8090"

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

`.github/workflows/tests-run.yml` runs on pushes to `main` and `feature/**`, on a monthly schedule, and on manual dispatch. The workflow:

1. Resolves the browser and profile matrix.
2. Starts the PrestaShop container.
3. Runs Checkstyle and PMD.
4. Installs the Linux dependencies required for Playwright WebKit.
5. Runs the Gradle test suite.
6. Uploads the Allure report artifact even when an earlier step fails.

Manual workflow dispatch accepts the test tag, browsers, profiles, and thread count. Pushes to `main` and scheduled runs execute the full browser/profile coverage; feature-branch pushes use the default Chromium desktop configuration.

## Scenario documentation

Business-level descriptions of the storefront scenarios are in `docs/scenarios/storefront/`. Keep these documents aligned when adding or materially changing a test scenario.
