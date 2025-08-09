# Exchange Rate Query Microservice - Running Instructions

## Starting the Application

To start the application in development mode, run:

```shell
./gradlew quarkusDev
```

This will start the Quarkus application on port 8080. The application will be accessible at http://localhost:8080.

## Testing the Application

### Automated Tests

Run the automated tests with:

```shell
./gradlew test
```

Note: One test is disabled as it requires connection to the external exchange rate API.

### Manual Testing

A test script is provided to manually test the exchange rate endpoint:

```shell
./test-exchange-rate.sh
```

This script will:
1. Test the endpoint with a valid DNI
2. Test the endpoint without a DNI (should return 400 Bad Request)
3. Test the query limit by making 11 requests with the same DNI (the 11th should return 429 Too Many Requests)

### API Endpoint

The main endpoint is:

```
GET /exchange-rate?dni={dni}
```

Where `{dni}` is the client's Document National Identity.

## Expected Behavior

1. When querying with a valid DNI, you should receive a 200 OK response with the current exchange rate.
2. When querying without a DNI, you should receive a 400 Bad Request response.
3. When making more than 10 queries with the same DNI in a day, you should receive a 429 Too Many Requests response.

## Troubleshooting

- If you encounter connection issues with the external API, check your internet connection.
- The application uses an in-memory H2 database, so data will be lost when the application is restarted.
- The daily query limit is set to 10 in the application.properties file.