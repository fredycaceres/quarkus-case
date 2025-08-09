package com.nttdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the exchange rate API response.
 * Based on the response from free.e-api.net.pe/tipo-cambio/today.json
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    @JsonProperty("fecha")
    private String date;

    @JsonProperty("compra")
    private Double buyRate;

    @JsonProperty("venta")
    private Double sellRate;

    @JsonProperty("origen")
    private String source;

    @JsonProperty("moneda")
    private String currency;

    // Default constructor required for Jackson
    public ExchangeRateResponse() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(Double buyRate) {
        this.buyRate = buyRate;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}