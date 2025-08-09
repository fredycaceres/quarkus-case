# Exchange Rate API Postman Collection

This directory contains a Postman collection and environment for testing the Exchange Rate API.

## Files

- `exchange-rate-api.postman_collection.json`: The Postman collection with API requests
- `exchange-rate-api.postman_environment.json`: Environment variables for the collection

## Setup Instructions

1. Install [Postman](https://www.postman.com/downloads/) if you haven't already.
2. Import the collection:
   - Open Postman
   - Click "Import" in the top left
   - Select the `exchange-rate-api.postman_collection.json` file
3. Import the environment:
   - Click "Import" again
   - Select the `exchange-rate-api.postman_environment.json` file
4. Select the environment:
   - In the top right corner of Postman, click the environment dropdown
   - Select "Exchange Rate API - Local"

## Using the Collection

The collection contains one request:

### Get Exchange Rate

- **Endpoint**: GET `/exchange-rate`
- **Required Parameters**: `dni` (Document National Identity)
- **Description**: Retrieves the current exchange rate information
- **Daily Limit**: 10 requests per DNI per day

### Example Responses

The collection includes example responses for:
- Successful request (200 OK)
- Too many requests (429 Too Many Requests)
- Missing DNI parameter (400 Bad Request)

## Customizing the Environment

You can modify the environment variables:

- `baseUrl`: The base URL of the API (default: http://localhost:8080)
- `dni`: The DNI value to use in requests (default: 12345678)

To modify:
1. Click on the "Environments" tab in Postman
2. Select "Exchange Rate API - Local"
3. Edit the values as needed
4. Click "Update"

## Running the Application

Make sure the Quarkus application is running before testing the API:

```bash
./gradlew quarkusDev
```

The application will be available at http://localhost:8080.