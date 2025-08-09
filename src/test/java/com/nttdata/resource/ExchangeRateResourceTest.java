package com.nttdata.resource;

import com.nttdata.dto.ExchangeRateResponse;
import com.nttdata.service.ExchangeRateService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.ContentType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ExchangeRateResourceTest {

    @InjectSpy
    ExchangeRateService exchangeRateService;

    @Test
    public void testGetExchangeRateWithValidDni() {
        // Prepare mock response
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setDate("2025-08-09");
        mockResponse.setBuyRate(3.75);
        mockResponse.setSellRate(3.78);
        mockResponse.setSource("SBS");
        mockResponse.setCurrency("USD");

        // Configure mock
        when(exchangeRateService.getExchangeRate("12345678")).thenReturn(mockResponse);

        // Test with valid DNI
        given()
            .when()
            .get("/exchange-rate?dni=12345678")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("fecha", equalTo("2025-08-09"))
            .body("compra", equalTo(3.75F))
            .body("venta", equalTo(3.78F))
            .body("origen", equalTo("SBS"))
            .body("moneda", equalTo("USD"));
    }

    @Test
    public void testGetExchangeRateWithoutDni() {
        // Test without DNI (should fail validation)
        given()
            .when()
            .get("/exchange-rate")
            .then()
            .statusCode(400) // Bad Request due to missing required parameter
            .contentType(ContentType.JSON)
            .body("violations[0].message", containsString("DNI is required"));
    }

    @Test
    public void testGetExchangeRateWithEmptyDni() {
        // Test with empty DNI (should fail validation)
        given()
            .when()
            .get("/exchange-rate?dni=")
            .then()
            .statusCode(400) // Bad Request due to empty required parameter
            .contentType(ContentType.JSON)
            .body("violations[0].message", containsString("DNI is required"));
    }

    @Test
    public void testGetExchangeRateWithDailyLimitExceeded() {
        // Configure mock to throw exception for daily limit exceeded
        when(exchangeRateService.getExchangeRate("87654321"))
            .thenThrow(new WebApplicationException(
                "Daily query limit exceeded for DNI: 87654321",
                Response.Status.TOO_MANY_REQUESTS));

        // Test with DNI that has exceeded daily limit
        given()
            .when()
            .get("/exchange-rate?dni=87654321")
            .then()
            .statusCode(429); // Too Many Requests
    }

    @Test
    public void testGetExchangeRateWithApiError() {
        // Configure mock to throw exception for API error
        when(exchangeRateService.getExchangeRate("99999999"))
            .thenThrow(new WebApplicationException(
                "Error fetching exchange rate: API unavailable",
                Response.Status.INTERNAL_SERVER_ERROR));

        // Test with DNI that triggers API error
        given()
            .when()
            .get("/exchange-rate?dni=99999999")
            .then()
            .statusCode(500); // Internal Server Error
    }
}