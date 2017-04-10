## Continuous Integration
[![Build Status](https://travis-ci.org/tutabeier/acmeinvoice.svg?branch=master)](https://travis-ci.org/tutabeier/acmeinvoice)

Build history in [Travis CI](https://travis-ci.org/tutabeier/acmeinvoice/).

## Setting up

### Requirements:
- Java 8
- Docker
- Git

### Running the application

- `git clone https://github.com/tutabeier/acmeinvoice.git acme-invoice`
- `cd acme-invoice`

You can simply run `./go.sh`, which will run:
- `docker build -t postgres-acme db`
- `docker run --name acme-invoice -p 5432:5432 -e POSTGRES_PASSWORD=password -d postgres-acme`
- `./gradlew bootRun`

#### Setting up data:
The application provides three endpoints: `/customers`, `/address` and `/invoices`.
You can populate the database using these three endpois or a shell script to do.
After the application is up and running, you can populate the database via Rest API:
- `cd setup/`
- `./setupData.sh`

For the sake of comprehension, the data used in `setupData.sh` is the same used in the `InvoiceEndToEnd.class`.
The filters work together, so you can use one, two, three or four.
Examples:
- [http://localhost:9000/v1.0/invoices/?customerId=1&filter=shop&month=12](http://localhost:9000/v1.0/invoices/?customerId=1&filter=shop&month=12)
- [http://localhost:9000/v1.0/invoices/?customerId=1&month=3](http://localhost:9000/v1.0/invoices/?customerId=1&month=3)
- [http://localhost:9000/v1.0/invoices/?customerId=4](http://localhost:9000/v1.0/invoices/?customerId=4)
- [http://localhost:9000/v1.0/invoices/?customerId=4&filter=shop](http://localhost:9000/v1.0/invoices/?customerId=4&filter=shop)

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