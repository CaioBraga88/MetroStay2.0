package com.trabalho.crud.core.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table( name = "\"Reserva\"")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroDoQuarto;

    private LocalDate dataInicioReserva;

    private LocalDate dataFinalReserva;

    private Long hospedeId;

    public Reserva() {
    }

    public Reserva(Long id, String numeroDoQuarto, LocalDate dataInicioReserva, LocalDate dataFinalReserva, Long hospedeId) {
        this.id = id;
        this.numeroDoQuarto = numeroDoQuarto;
        this.dataInicioReserva = dataInicioReserva;
        this.dataFinalReserva = dataFinalReserva;
        this.hospedeId = hospedeId;
    }

    private Reserva(Builder builder) {
        this.id = builder.id;
        this.numeroDoQuarto = builder.numeroDoQuarto;
        this.dataInicioReserva = builder.dataInicioReserva;
        this.dataFinalReserva = builder.dataFinalReserva;
        this.hospedeId = builder.hospedeId;
    }

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

    public void setHospedeId( Long hospedeId) {
        this.hospedeId = hospedeId;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // =========================================================================
    // Implementação de hashCode, equals e toString para estabilidade da Entity
    // =========================================================================

    /**
     * Define a igualdade com base no campo 'id'.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Importante: para JPA, deve-se usar o método getClass()
        if (o == null || getClass() != o.getClass()) return false; 
        Reserva reserva = (Reserva) o;
        // Para entidades JPA, a igualdade deve ser determinada pelo ID
        return Objects.equals(id, reserva.id);
    }

    /**
     * Gera o HashCode baseado no campo 'id'.
     */
    @Override
    public int hashCode() {
        // Se o id for nulo (antes de persistir), usamos uma constante para evitar erros de JPA
        return id != null ? Objects.hash(id) : 31;
    }

    /**
     * Gera uma representação em String que inclui todos os campos.
     */
    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", numeroDoQuarto='" + numeroDoQuarto + '\'' +
                ", dataInicioReserva=" + dataInicioReserva +
                ", dataFinalReserva=" + dataFinalReserva +
                ", hospedeId=" + hospedeId +
                '}';
    }


    public static class Builder {

        private Long id;

        private String numeroDoQuarto;

        private LocalDate dataInicioReserva;

        private LocalDate dataFinalReserva;

        private Long hospedeId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder numeroDoQuarto(String numeroDoQuarto) {
            this.numeroDoQuarto = numeroDoQuarto;
            return this;
        }

        public Builder dataInicioReserva(LocalDate dataInicioReserva) {
            this.dataInicioReserva = dataInicioReserva;
            return this;
        }

        public Builder dataFinalReserva(LocalDate dataFinalReserva) {
            this.dataFinalReserva = dataFinalReserva;
            return this;
        }

        public Builder hospedeID(Long hospedeId) {
            this.hospedeId = hospedeId;
            return this;
        }


        public Reserva build() {
            return new Reserva(this);
        }

    }

}
