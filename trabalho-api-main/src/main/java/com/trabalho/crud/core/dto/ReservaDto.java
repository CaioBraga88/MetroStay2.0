package com.trabalho.crud.core.dto; 

import java.time.LocalDate;


public class ReservaDto {

    private Long id;
    private String numeroDoQuarto;
    private LocalDate dataInicioReserva;
    private LocalDate dataFinalReserva;
    private Long hospedeId;

    // Construtor padrão
    public ReservaDto() {
    }

    // Getters e Setters (Camel Case correto)
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDoQuarto() {
        return numeroDoQuarto;
    }

    public void setNumeroDoQuarto(String numeroDoQuarto) {
        this.numeroDoQuarto = numeroDoQuarto;
    }

    public LocalDate getDataInicioReserva() {
        return dataInicioReserva;
    }

    public void setDataInicioReserva(LocalDate dataInicioReserva) {
        this.dataInicioReserva = dataInicioReserva;
    }

    public LocalDate getDataFinalReserva() {
        return dataFinalReserva;
    }

    public void setDataFinalReserva(LocalDate dataFinalReserva) {
        this.dataFinalReserva = dataFinalReserva;
    }

    public Long getHospedeId() {
        return hospedeId;
    }

    public void setHospedeId(Long hospedeId) {
        this.hospedeId = hospedeId;
    }
}
