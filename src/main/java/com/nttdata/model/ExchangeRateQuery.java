package com.nttdata.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity to track exchange rate queries per client.
 */
@Entity
@Table(name = "exchange_rate_queries")
public class ExchangeRateQuery extends PanacheEntity {

    @Column(name = "dni", nullable = false)
    private String dni;

    @Column(name = "query_date", nullable = false)
    private LocalDateTime queryDateTime;

    @Column(name = "query_date_only", nullable = false)
    private LocalDate queryDate;

    public ExchangeRateQuery() {
    }

    public ExchangeRateQuery(String dni) {
        this.dni = dni;
        this.queryDateTime = LocalDateTime.now();
        this.queryDate = this.queryDateTime.toLocalDate();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDateTime getQueryDateTime() {
        return queryDateTime;
    }

    public void setQueryDateTime(LocalDateTime queryDateTime) {
        this.queryDateTime = queryDateTime;
    }

    public LocalDate getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(LocalDate queryDate) {
        this.queryDate = queryDate;
    }

    /**
     * Count queries by DNI and date.
     *
     * @param dni  the client's DNI
     * @param date the date to count queries for
     * @return the number of queries for the given DNI and date
     */
    public static long countByDniAndDate(String dni, LocalDate date) {
        return count("dni = ?1 and queryDate = ?2", dni, date);
    }

    /**
     * Create and persist a new query record.
     *
     * @param dni the client's DNI
     * @return the created query record
     */
    public static ExchangeRateQuery createQuery(String dni) {
        ExchangeRateQuery query = new ExchangeRateQuery(dni);
        query.persist();
        return query;
    }
}