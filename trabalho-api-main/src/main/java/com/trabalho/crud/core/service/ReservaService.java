package com.trabalho.crud.core.service;

import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        
    }

    /**
     * Retorna todas as reservas cadastradas.
     * @return Uma lista de todas as Reservas.
     */
    public List<Reserva> buscarTodas() {
        return reservaRepository.findAll();
    }
    
    /**
     * Tenta criar uma nova reserva após executar as validações de regras de negócio.
     * * @param novaReserva A Reserva a ser salva.
     * @return A Reserva persistida no banco de dados.
     * @throws ValidacaoReservaException se alguma regra de negócio for violada.
     */

    public Reserva criar(Reserva novaReserva) {
        
        validarDatas(novaReserva);
        validarDisponibilidade(novaReserva);
        validarHospede(novaReserva.getHospedeId()); 
        
        return reservaRepository.save(novaReserva);
    }
    
    /**
     * Retorna uma reserva pelo seu ID.
     * * @param id O ID da reserva.
     * @return A Reserva encontrada.
     * @throws ResourceNotFoundException se a reserva não for encontrada.
     */
    
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva com ID " + id + " não encontrada."));
    }
    
    /**
     * Atualiza os dados de uma reserva existente.
     * * @param id O ID da reserva a ser atualizada.
     * @param reservaDetalhes Os novos detalhes da reserva.
     * @return A Reserva atualizada.
     * @throws ResourceNotFoundException se a reserva não for encontrada.
     * @throws ValidacaoReservaException se as novas datas violarem alguma regra.
     */
    public Reserva atualizar(Long id, Reserva reservaDetalhes) {
        Reserva reservaExistente = buscarPorId(id); // Já valida se existe
        
        // Aplica os novos detalhes
        reservaExistente.setNumeroDoQuarto(reservaDetalhes.getNumeroDoQuarto());
        reservaExistente.setDataInicioReserva(reservaDetalhes.getDataInicioReserva());
        reservaExistente.setDataFinalReserva(reservaDetalhes.getDataFinalReserva());
        reservaExistente.setHospedeId(reservaDetalhes.getHospedeId());
        
        // Revalida as datas e disponibilidade APÓS a modificação
        validarDatas(reservaExistente);
        validarDisponibilidadeDuranteAtualizacao(reservaExistente);
        validarHospede(reservaExistente.getHospedeId());

        return reservaRepository.save(reservaExistente);
    }
    
    /**
     * Deleta uma reserva pelo seu ID.
     * * @param id O ID da reserva a ser deletada.
     * @throws ResourceNotFoundException se a reserva não for encontrada.
     */
    public void deletar(Long id) {
        Reserva reserva = buscarPorId(id); // Garante que a reserva existe antes de deletar
        reservaRepository.delete(reserva);
    }
    
    // =========================================================================
    // Métodos de Validação de Regra de Negócio
    // =========================================================================
    
    private void validarDatas(Reserva reserva) {
        LocalDate hoje = LocalDate.now();

        // Regra 1: Data de Check-out não pode ser antes ou igual à data de Check-in.
        if (!reserva.getDataFinalReserva().isAfter(reserva.getDataInicioReserva())) {
            throw new ValidacaoReservaException("A data final da reserva deve ser posterior à data de início.");
        }

        // Regra 2: Não é permitido criar reservas com data de check-in no passado.
        // Permite reservas para HOJE, mas não para datas anteriores.
        if (reserva.getDataInicioReserva().isBefore(hoje)) {
            throw new ValidacaoReservaException("Não é permitido agendar reservas para datas passadas.");
        }
    }
    
    /**
     * Regra 3: Checa se o quarto está ocupado no período desejado para uma nova reserva.
     */
    private void validarDisponibilidade(Reserva novaReserva) {

        List<Reserva> conflitos = reservaRepository
            .findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(
                novaReserva.getNumeroDoQuarto(), 
                novaReserva.getDataInicioReserva(), 
                novaReserva.getDataFinalReserva()
            );

        if (!conflitos.isEmpty()) {
            throw new ValidacaoReservaException("O quarto " + novaReserva.getNumeroDoQuarto() 
                                              + " já está reservado no período de " 
                                              + novaReserva.getDataInicioReserva() + " a " 
                                              + novaReserva.getDataFinalReserva() + ".");
        }
    }
    
    /**
     * Checa se o quarto está ocupado no período desejado durante a atualização, 
     * ignorando a própria reserva que está sendo atualizada.
     */
    private void validarDisponibilidadeDuranteAtualizacao(Reserva reservaAtualizada) {
        // Ao atualizar, precisamos ignorar a própria reserva do conflito
        List<Reserva> conflitos = reservaRepository
            .findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(
                reservaAtualizada.getNumeroDoQuarto(), 
                reservaAtualizada.getDataInicioReserva(), 
                reservaAtualizada.getDataFinalReserva()
            );

        // Se houver conflitos, verificamos se todos os conflitos são a própria reserva
        for (Reserva conflito : conflitos) {
            if (!conflito.getId().equals(reservaAtualizada.getId())) {
                throw new ValidacaoReservaException("O quarto " + reservaAtualizada.getNumeroDoQuarto() 
                                                  + " já está reservado por outra pessoa neste novo período.");
            }
        }
    }

    // Regra 4: Simulação da validação de Hóspede
    private void validarHospede(Long hospedeId) {
        // Implementação simulada: deve ser um ID válido (não nulo e positivo)
        if (hospedeId == null || hospedeId <= 0) {
            throw new ValidacaoReservaException("O ID do Hóspede é obrigatório e deve ser válido.");
        }
    }

}