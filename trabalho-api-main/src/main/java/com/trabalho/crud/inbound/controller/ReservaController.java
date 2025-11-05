package com.trabalho.crud.inbound.controller; 

import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.dto.ReservaDto;
import com.trabalho.crud.core.mapper.ReservaMapper;
import com.trabalho.crud.core.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; 

import java.util.List;

/**
 * Controller REST para gerenciar operações de CRUD para a entidade Reserva.
 * Este controller é o ponto de entrada da API e utiliza DTOs para comunicação.
 */
@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaMapper reservaMapper;

    @Autowired
    public ReservaController(ReservaService reservaService, ReservaMapper reservaMapper) {
        this.reservaService = reservaService;
        this.reservaMapper = reservaMapper;
    }

    /**
     * Cria uma nova reserva.
     * * @param reservaDto O DTO da Reserva a ser criada.
     * @return ResponseEntity com o DTO da Reserva criada e status HTTP 201 (Created).
     * @throws com.trabalho.crud.core.service.ValidacaoReservaException se as regras de negócio forem violadas.
     */
    @PostMapping
    public ResponseEntity<ReservaDto> criarReserva(@Valid @RequestBody ReservaDto reservaDto) {
        Reserva reserva = reservaMapper.toEntity(reservaDto);
        Reserva novaReserva = reservaService.criar(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaMapper.toDto(novaReserva));
    }

    /**
     * Busca uma reserva pelo ID.
     * * @param id O ID da reserva.
     * @return ResponseEntity com o DTO da Reserva e status HTTP 200 (OK).
     * @throws com.trabalho.crud.core.service.ResourceNotFoundException se a reserva não for encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> buscarReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(reservaMapper.toDto(reserva));
    }
    
    /**
     * Busca todas as reservas cadastradas.
     * @return ResponseEntity com a lista de DTOs das Reservas e status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ReservaDto>> buscarTodasReservas() {
        List<Reserva> reservas = reservaService.buscarTodas();
        return ResponseEntity.ok(reservaMapper.toDtoList(reservas));
    }

    /**
     * Atualiza uma reserva existente pelo ID.
     * * @param id O ID da reserva a ser atualizada.
     * @param reservaDto O DTO com os novos detalhes da Reserva.
     * @return ResponseEntity com o DTO da Reserva atualizada e status HTTP 200 (OK).
     * @throws com.trabalho.crud.core.service.ResourceNotFoundException se a reserva não for encontrada.
     * @throws com.trabalho.crud.core.service.ValidacaoReservaException se as regras de negócio forem violadas.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDto> atualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaDto reservaDto) {
        Reserva reserva = reservaMapper.toEntity(reservaDto);
        Reserva reservaAtualizada = reservaService.atualizar(id, reserva);
        return ResponseEntity.ok(reservaMapper.toDto(reservaAtualizada));
    }

    /**
     * Deleta uma reserva pelo ID.
     * * @param id O ID da reserva a ser deletada.
     * @return ResponseEntity com status HTTP 204 (No Content).
     * @throws com.trabalho.crud.core.service.ResourceNotFoundException se a reserva não for encontrada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        reservaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
