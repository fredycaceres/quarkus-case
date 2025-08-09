package com.nttdata.model;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ExchangeRateQueryTest {

    @Test
    @Transactional
    public void testCreateQuery() {
        // Create a new query
        String dni = "12345678";
        ExchangeRateQuery query = ExchangeRateQuery.createQuery(dni);
        
        // Verify the query was created correctly
        assertNotNull(query);
        assertNotNull(query.id);
        assertEquals(dni, query.getDni());
        assertNotNull(query.getQueryDateTime());
        assertNotNull(query.getQueryDate());
        assertEquals(LocalDate.now(), query.getQueryDate());
    }
    
    @Test
    @Transactional
    public void testCountByDniAndDate() {
        // Create some test queries
        String dni = "87654321";
        LocalDate today = LocalDate.now();
        
        // Create 3 queries for today
        ExchangeRateQuery.createQuery(dni);
        ExchangeRateQuery.createQuery(dni);
        ExchangeRateQuery.createQuery(dni);
        
        // Create a query with a different DNI
        ExchangeRateQuery.createQuery("99999999");
        
        // Count queries for the test DNI and today
        long count = ExchangeRateQuery.countByDniAndDate(dni, today);
        
        // Verify the count
        assertEquals(3, count);
    }
    
    @Test
    @Transactional
    public void testCountByDniAndDateWithNoQueries() {
        // Count queries for a DNI that doesn't exist
        String dni = "00000000";
        LocalDate today = LocalDate.now();
        
        long count = ExchangeRateQuery.countByDniAndDate(dni, today);
        
        // Verify the count is zero
        assertEquals(0, count);
    }
    
    @Test
    @Transactional
    public void testQueryConstructor() {
        // Test the constructor
        String dni = "11223344";
        ExchangeRateQuery query = new ExchangeRateQuery(dni);
        
        // Verify the fields
        assertEquals(dni, query.getDni());
        assertNotNull(query.getQueryDateTime());
        assertNotNull(query.getQueryDate());
        assertEquals(LocalDate.now(), query.getQueryDate());
    }
    
    @Test
    @Transactional
    public void testSettersAndGetters() {
        // Create a query with default constructor
        ExchangeRateQuery query = new ExchangeRateQuery();
        
        // Set values
        String dni = "55667788";
        LocalDateTime dateTime = LocalDateTime.now().minusDays(1);
        LocalDate date = dateTime.toLocalDate();
        
        query.setDni(dni);
        query.setQueryDateTime(dateTime);
        query.setQueryDate(date);
        
        // Verify getters
        assertEquals(dni, query.getDni());
        assertEquals(dateTime, query.getQueryDateTime());
        assertEquals(date, query.getQueryDate());
    }
}