package com.nttdata.client;

import com.nttdata.dto.ExchangeRateResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client for the exchange rate API.
 */
@Path("/")
@RegisterRestClient(configKey = "exchange-rate-api")
public interface ExchangeRateClient {

    /**
     * Get the current exchange rate.
     *
     * @return the exchange rate response
     */
    @GET
    @Path("/tipo-cambio/today.json")
    @Produces(MediaType.APPLICATION_JSON)
    ExchangeRateResponse getExchangeRate();
}