package com.nttdata.service;

import com.nttdata.client.ExchangeRateClient;
import com.nttdata.dto.ExchangeRateResponse;
import com.nttdata.model.ExchangeRateQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ExchangeRateServiceTest {

    @Inject
    ExchangeRateService exchangeRateService;

    @InjectSpy
    @RestClient
    ExchangeRateClient exchangeRateClient;

    private ExchangeRateResponse mockResponse;

    @BeforeEach
    public void setup() {
        // Prepare mock response
        mockResponse = new ExchangeRateResponse();
        mockResponse.setDate("2025-08-09");
        mockResponse.setBuyRate(3.75);
        mockResponse.setSellRate(3.78);
        mockResponse.setSource("SBS");
        mockResponse.setCurrency("USD");

        // Configure mock client
        when(exchangeRateClient.getExchangeRate()).thenReturn(mockResponse);

        // Set daily query limit for testing
        exchangeRateService.dailyQueryLimit = 3;
    }

    @Test
    public void testGetExchangeRateSuccess() {
        // Mock the static methods in ExchangeRateQuery
        try (var queryCountMock = mockStatic(ExchangeRateQuery.class)) {
            // Configure mocks
            queryCountMock.when(() -> ExchangeRateQuery.countByDniAndDate(anyString(), any(LocalDate.class)))
                    .thenReturn(0L);
            queryCountMock.when(() -> ExchangeRateQuery.createQuery(anyString()))
                    .thenReturn(new ExchangeRateQuery("12345678"));

            // Call the service
            ExchangeRateResponse response = exchangeRateService.getExchangeRate("12345678");

            // Verify the response
            assertNotNull(response);
            assertEquals("2025-08-09", response.getDate());
            assertEquals(3.75, response.getBuyRate());
            assertEquals(3.78, response.getSellRate());
            assertEquals("SBS", response.getSource());
            assertEquals("USD", response.getCurrency());

            // Verify interactions
            queryCountMock.verify(() -> ExchangeRateQuery.countByDniAndDate("12345678", LocalDate.now()));
            queryCountMock.verify(() -> ExchangeRateQuery.createQuery("12345678"));
            verify(exchangeRateClient, times(1)).getExchangeRate();
        }
    }

    @Test
    public void testGetExchangeRateDailyLimitExceeded() {
        // Mock the static methods in ExchangeRateQuery
        try (var queryCountMock = mockStatic(ExchangeRateQuery.class)) {
            // Configure mocks to return count equal to the limit
            queryCountMock.when(() -> ExchangeRateQuery.countByDniAndDate(anyString(), any(LocalDate.class)))
                    .thenReturn(3L); // Equal to the limit set in setup

            // Call the service and expect exception
            WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
                exchangeRateService.getExchangeRate("87654321");
            });

            // Verify the exception
            assertEquals(429, exception.getResponse().getStatus()); // Too Many Requests
            assertTrue(exception.getMessage().contains("Daily query limit exceeded"));

            // Verify interactions
            queryCountMock.verify(() -> ExchangeRateQuery.countByDniAndDate("87654321", LocalDate.now()));
            // Verify that createQuery was not called
            queryCountMock.verify(() -> ExchangeRateQuery.createQuery(anyString()), never());
            // Verify that the client was not called
            verify(exchangeRateClient, never()).getExchangeRate();
        }
    }

    @Test
    public void testGetExchangeRateApiError() {
        // Mock the static methods in ExchangeRateQuery
        try (var queryCountMock = mockStatic(ExchangeRateQuery.class)) {
            // Configure mocks
            queryCountMock.when(() -> ExchangeRateQuery.countByDniAndDate(anyString(), any(LocalDate.class)))
                    .thenReturn(0L);
            queryCountMock.when(() -> ExchangeRateQuery.createQuery(anyString()))
                    .thenReturn(new ExchangeRateQuery("99999999"));

            // Configure client to throw exception
            when(exchangeRateClient.getExchangeRate()).thenThrow(new RuntimeException("API unavailable"));

            // Call the service and expect exception
            WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
                exchangeRateService.getExchangeRate("99999999");
            });

            // Verify the exception
            assertEquals(500, exception.getResponse().getStatus()); // Internal Server Error
            assertTrue(exception.getMessage().contains("Error fetching exchange rate"));

            // Verify interactions
            queryCountMock.verify(() -> ExchangeRateQuery.countByDniAndDate("99999999", LocalDate.now()));
            queryCountMock.verify(() -> ExchangeRateQuery.createQuery("99999999"));
            verify(exchangeRateClient, times(1)).getExchangeRate();
        }
    }
}