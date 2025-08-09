package com.nttdata.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for the exchange rate query request.
 */
public class ExchangeRateRequest {

    @NotBlank(message = "DNI is required")
    private String dni;

    // Default constructor
    public ExchangeRateRequest() {
    }

    public ExchangeRateRequest(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}