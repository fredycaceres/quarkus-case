# Exchange Rate Query Microservice

This project implements an exchange rate query microservice for Bank ABC using Quarkus, the Supersonic Subatomic Java Framework.

## Features

- Query exchange rates from an external API
- Track and limit the number of queries per client (by DNI)
- Validate input parameters
- Handle error cases gracefully

## API Endpoints

### GET /exchange-rate

Query the current exchange rate.

**Request Parameters:**
- `dni` (required): The client's DNI (Document National Identity)

**Response:**
```json
{
  "date": "2025-08-09",
  "buyRate": 3.75,
  "sellRate": 3.78,
  "source": "SBS",
  "currency": "USD"
}
```

**Error Responses:**
- 400 Bad Request: If the DNI parameter is missing or invalid
- 429 Too Many Requests: If the client has exceeded the daily query limit (10 queries per day)
- 500 Internal Server Error: If there's an error fetching the exchange rate from the external API

## Business Rules

1. Each client (identified by DNI) is limited to 10 exchange rate queries per day
2. All queries are recorded in the database for tracking purposes
3. The exchange rate is fetched from the external API: free.e-api.net.pe/tipo-cambio/today.json

## Technical Details

### Technologies Used

- JDK 21
- Quarkus
- Hibernate ORM with Panache
- REST Jackson
- REST Client Jackson
- H2 Database (in-memory for development)

### Project Structure

- `com.nttdata.model`: Entity classes for database operations
- `com.nttdata.dto`: Data Transfer Objects for API requests and responses
- `com.nttdata.client`: REST client for the external API
- `com.nttdata.service`: Business logic services
- `com.nttdata.resource`: REST endpoints

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Testing

The application includes unit tests for the REST endpoint, including:
- Testing with valid DNI
- Testing without DNI (validation error)
- Testing the query limit functionality

Run the tests with:

```shell script
./gradlew test
```

## Postman Collection

A Postman collection is provided to help test the API:

- `exchange-rate-api.postman_collection.json`: Collection with API requests
- `exchange-rate-api.postman_environment.json`: Environment variables for the collection
- `POSTMAN_README.md`: Detailed instructions for using the Postman collection

To use the Postman collection:

1. Import the collection and environment files into Postman
2. Select the "Exchange Rate API - Local" environment
3. Use the "Get Exchange Rate" request to test the API

See `POSTMAN_README.md` for detailed instructions.
