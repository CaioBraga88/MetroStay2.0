package com.trabalho.crud.core.mapper;

import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.dto.ReservaDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Componente responsável por mapear a Entidade Reserva para ReservaDto e vice-versa.
 */
@Component
public class ReservaMapper {

    /**
     * Converte uma Entidade Reserva para um DTO de Reserva.
     * @param reserva A entidade a ser convertida.
     * @return O ReservaDto correspondente.
     */
    public ReservaDto toDto(Reserva reserva) {
        if (reserva == null) {
            return null;
        }
        ReservaDto dto = new ReservaDto();
        dto.setId(reserva.getId());
        dto.setNumeroDoQuarto(reserva.getNumeroDoQuarto());
        dto.setDataInicioReserva(reserva.getDataInicioReserva());
        dto.setDataFinalReserva(reserva.getDataFinalReserva());
        dto.setHospedeId(reserva.getHospedeId());
        return dto;
    }

    /**
     * Converte um DTO de Reserva para uma Entidade Reserva.
     * @param dto O DTO a ser convertido.
     * @return A Entidade Reserva correspondente.
     */
    public Reserva toEntity(ReservaDto dto) {
        if (dto == null) {
            return null;
        }
        
        // Usando o Builder da Entidade para criar o objeto 
        return Reserva.builder()
            .id(dto.getId())
            .numeroDoQuarto(dto.getNumeroDoQuarto())
            .dataInicioReserva(dto.getDataInicioReserva())
            .dataFinalReserva(dto.getDataFinalReserva())
            .hospedeID(dto.getHospedeId())
            .build();
    }

    /**
     * Converte uma lista de Entidades Reserva para uma lista de DTOs.
     * @param reservas A lista de entidades.
     * @return A lista de DTOs.
     */
    public List<ReservaDto> toDtoList(List<Reserva> reservas) {
        // CORREÇÃO: Adicionada verificação de null para evitar NullPointerException.
        if (reservas == null) {
            return null; 
            // Alternativamente, se preferir uma lista vazia, use: return List.of();
        }
        return reservas.stream().map(this::toDto).collect(Collectors.toList());
    }
}
