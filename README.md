
## Running the application

From the project root folder:

- `docker build -t postgres-acme db`
- `docker run --name acme-invoice -p 5432:5432 -e POSTGRES_PASSWORD=password -d postgres-acme`
- `./gradlew bootRun`

## Running tests and generating reports

Running tests:

`./gradlew test`

Alternatively, one can run tests separately:
* `./gradlew unitTest`
* `./gradlew integrationTests`
* `./gradlew endToEndTests`

