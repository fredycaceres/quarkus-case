package com.nttdata.client;

import com.nttdata.dto.ExchangeRateResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ExchangeRateClient.
 * Note: These tests require an active internet connection and the external API to be available.
 * They are disabled by default and can be enabled with -Dtest.external.api=true
 */
@QuarkusTest
public class ExchangeRateClientTest {

    @Inject
    @RestClient
    ExchangeRateClient exchangeRateClient;

    /**
     * Test that the client can successfully call the external API.
     * This test is disabled by default to avoid external dependencies during regular test runs.
     */
    @Test
    @EnabledIfSystemProperty(named = "test.external.api", matches = "true")
    public void testGetExchangeRate() {
        // Call the external API
        ExchangeRateResponse response = exchangeRateClient.getExchangeRate();
        
        // Verify the response
        assertNotNull(response);
        assertNotNull(response.getDate());
        assertNotNull(response.getBuyRate());
        assertNotNull(response.getSellRate());
        assertNotNull(response.getSource());
        assertNotNull(response.getCurrency());
        
        // Log the response for debugging
        System.out.println("[DEBUG_LOG] Exchange Rate Response: " + 
                "Date=" + response.getDate() + 
                ", Buy=" + response.getBuyRate() + 
                ", Sell=" + response.getSellRate() + 
                ", Source=" + response.getSource() + 
                ", Currency=" + response.getCurrency());
    }
}