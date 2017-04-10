## Continuous Integration
[![Build Status](https://travis-ci.org/tutabeier/acmeinvoice.svg?branch=master)](https://travis-ci.org/tutabeier/acmeinvoice)

Build history in [Travis CI](https://travis-ci.org/tutabeier/acmeinvoice/).

## Running the application

From the project root folder:

- `docker build -t postgres-acme db`
- `docker run --name acme-invoice -p 5432:5432 -e POSTGRES_PASSWORD=password -d postgres-acme`
- `./gradlew bootRun`

## Running tests and generating reports

Running tests:
- `docker build -t postgres-acme db`
- `docker run --name acme-invoice-test -p 5433:5432 -e POSTGRES_PASSWORD=password -d postgres-acme`
- `./gradlew unitTest`
- `./gradlew integrationTests`
- `./gradlew endToEndTests`


For each test level (unit, integration, end2end) a Junit report will be generated:
- Unit tests: `build/reports/tests/unitTests/index.html`
- Integration tests: `build/reports/tests/integrationTests/index.html`
- End to end tests: `build/reports/tests/endToEndTests/index.html`

A Jacoco report (for code coverage) can be also generated:
- `./gradlew jacocoTestReport`

The reports will be placed at `build/reports/jacoco/test/html/index.html`.