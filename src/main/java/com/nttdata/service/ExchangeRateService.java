package com.nttdata.service;

import com.nttdata.client.ExchangeRateClient;
import com.nttdata.dto.ExchangeRateResponse;
import com.nttdata.model.ExchangeRateQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

/**
 * Service for handling exchange rate queries.
 */
@ApplicationScoped
public class ExchangeRateService {

    @Inject
    @RestClient
    ExchangeRateClient exchangeRateClient;

    @ConfigProperty(name = "exchange.rate.daily.limit", defaultValue = "10")
    int dailyQueryLimit;

    /**
     * Get the current exchange rate for a client.
     *
     * @param dni the client's DNI
     * @return the exchange rate response
     * @throws WebApplicationException if the client has exceeded the daily query limit
     */
    @Transactional
    public ExchangeRateResponse getExchangeRate(String dni) {
        // Check if the client has exceeded the daily query limit
        LocalDate today = LocalDate.now();
        long queryCount = ExchangeRateQuery.countByDniAndDate(dni, today);

        if (queryCount >= dailyQueryLimit) {
            throw new WebApplicationException(
                    "Daily query limit exceeded for DNI: " + dni,
                    Response.Status.TOO_MANY_REQUESTS);
        }

        // Record the query
        ExchangeRateQuery.createQuery(dni);

        // Get the exchange rate from the external API
        try {
            return exchangeRateClient.getExchangeRate();
        } catch (Exception e) {
            throw new WebApplicationException(
                    "Error fetching exchange rate: " + e.getMessage(),
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}