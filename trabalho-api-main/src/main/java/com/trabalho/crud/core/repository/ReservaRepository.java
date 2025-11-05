package com.trabalho.crud.core.repository;

import com.trabalho.crud.core.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    /**
     * Método customizado pelo Spring Data JPA. Ele é interpretado para buscar reservas
     * que causam conflito de datas no mesmo quarto.
     * * O critério é:
     * 1. O número do quarto deve ser o especificado.
     * 2. A data final da reserva existente deve ser DEPOIS (After) da data de início desejada.
     * 3. E a data de início da reserva existente deve ser ANTES (Before) da data final desejada.
     * * Se esta consulta retornar qualquer resultado, significa que há um conflito de datas.
     */
    List<Reserva> findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(
        String numeroDoQuarto, 
        LocalDate dataInicioDesejada, 
        LocalDate dataFinalDesejada
    );
}
