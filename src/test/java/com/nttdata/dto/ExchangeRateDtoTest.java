package com.nttdata.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DTO classes.
 */
public class ExchangeRateDtoTest {

    @Test
    public void testExchangeRateRequest() {
        // Test default constructor
        ExchangeRateRequest request1 = new ExchangeRateRequest();
        assertNull(request1.getDni());
        
        // Test parameterized constructor
        String dni = "12345678";
        ExchangeRateRequest request2 = new ExchangeRateRequest(dni);
        assertEquals(dni, request2.getDni());
        
        // Test setter
        String newDni = "87654321";
        request1.setDni(newDni);
        assertEquals(newDni, request1.getDni());
    }
    
    @Test
    public void testExchangeRateResponse() {
        // Test default constructor
        ExchangeRateResponse response = new ExchangeRateResponse();
        
        // Test setters and getters
        String date = "2025-08-09";
        Double buyRate = 3.75;
        Double sellRate = 3.78;
        String source = "SBS";
        String currency = "USD";
        
        response.setDate(date);
        response.setBuyRate(buyRate);
        response.setSellRate(sellRate);
        response.setSource(source);
        response.setCurrency(currency);
        
        // Verify getters
        assertEquals(date, response.getDate());
        assertEquals(buyRate, response.getBuyRate());
        assertEquals(sellRate, response.getSellRate());
        assertEquals(source, response.getSource());
        assertEquals(currency, response.getCurrency());
    }
}