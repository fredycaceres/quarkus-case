package com.nttdata.resource;

import com.nttdata.dto.ExchangeRateResponse;
import com.nttdata.service.ExchangeRateService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for exchange rate queries.
 */
@Path("/exchange-rate")
@Valid
public class ExchangeRateResource {

    @Inject
    ExchangeRateService exchangeRateService;

    /**
     * Get the current exchange rate.
     *
     * @param dni the client's DNI (required)
     * @return the exchange rate response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ValidateOnExecution
    public Response getExchangeRate(@NotBlank(message = "DNI is required") @QueryParam("dni") String dni) {
        ExchangeRateResponse response = exchangeRateService.getExchangeRate(dni);
        return Response.ok(response).build();
    }
}