docker build -t postgres-acme db
docker run --name acme-invoice -p 5432:5432 -e POSTGRES_PASSWORD=password -d postgres-acme
./gradlew bootRun
