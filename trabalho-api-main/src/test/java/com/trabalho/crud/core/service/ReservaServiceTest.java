package com.trabalho.crud.core.service;

import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    // Simula a dependência do repositório
    @Mock
    private ReservaRepository reservaRepository;

    // Injeta a dependência simulada na classe a ser testada
    @InjectMocks
    private ReservaService reservaService;

    // Variáveis de teste
    private Reserva reservaValida;
    private LocalDate hoje;
    private LocalDate amanha;

    @BeforeEach
    void setUp() {
        hoje = LocalDate.now();
        amanha = hoje.plusDays(1);

        // Cria uma Reserva válida para ser usada na maioria dos testes
        reservaValida = new Reserva();
        reservaValida.setId(1L);
        reservaValida.setNumeroDoQuarto("101");
        reservaValida.setDataInicioReserva(amanha.plusDays(1));
        reservaValida.setDataFinalReserva(amanha.plusDays(3));
        reservaValida.setHospedeId(10L);
    }

    // -------------------------------------------------------------------------
    // TESTES DE CRUD BÁSICO (SUCESSO)
    // -------------------------------------------------------------------------

    @Test
    void criar_DeveCriarReservaComSucesso() {
        // Dado que todas as validações de disponibilidade e datas passarão
        when(reservaRepository.findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaValida);

        Reserva reservaCriada = reservaService.criar(reservaValida);

        assertNotNull(reservaCriada);
        assertEquals("101", reservaCriada.getNumeroDoQuarto());
        // Verifica se o método save foi chamado
        verify(reservaRepository, times(1)).save(reservaValida);
    }

    @Test
    void buscarPorId_DeveRetornarReservaExistente() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaValida));

        Reserva encontrada = reservaService.buscarPorId(1L);

        assertNotNull(encontrada);
        assertEquals(1L, encontrada.getId());
    }

    @Test
    void buscarTodas_DeveRetornarListaDeReservas() {
        List<Reserva> lista = List.of(reservaValida, new Reserva());
        when(reservaRepository.findAll()).thenReturn(lista);

        List<Reserva> resultado = reservaService.buscarTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
    }

    @Test
    void atualizar_DeveAtualizarReservaComSucesso() {
        Reserva detalhesAtualizados = new Reserva();
        detalhesAtualizados.setNumeroDoQuarto("202");
        detalhesAtualizados.setDataInicioReserva(amanha.plusDays(5));
        detalhesAtualizados.setDataFinalReserva(amanha.plusDays(7));
        detalhesAtualizados.setHospedeId(10L); // Hospede ID deve ser mantido ou atualizado

        // 1. Simula a busca da reserva existente
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaValida));

        // 2. Simula a validação de disponibilidade (nenhum conflito exceto ela mesma)
        when(reservaRepository.findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(reservaValida));

        // 3. Simula o salvamento
        when(reservaRepository.save(any(Reserva.class))).thenReturn(detalhesAtualizados);

        Reserva reservaAtualizada = reservaService.atualizar(1L, detalhesAtualizados);

        assertEquals("202", reservaAtualizada.getNumeroDoQuarto());
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void deletar_DeveDeletarReservaComSucesso() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaValida));
        doNothing().when(reservaRepository).delete(reservaValida);

        reservaService.deletar(1L);

        // Verifica se a busca e a deleção foram chamadas
        verify(reservaRepository, times(1)).findById(1L);
        verify(reservaRepository, times(1)).delete(reservaValida);
    }


    // -------------------------------------------------------------------------
    // TESTES DE FALHA (EXCEÇÕES)
    // -------------------------------------------------------------------------

    @Test
    void buscarPorId_DeveLancarResourceNotFoundException() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.buscarPorId(99L));
    }

    @Test
    void criar_DeveLancarValidacaoExceptionSeDataFinalAntesOuIgualInicio() {
        reservaValida.setDataInicioReserva(amanha.plusDays(5));
        reservaValida.setDataFinalReserva(amanha.plusDays(5)); // Datas iguais

        assertThrows(ValidacaoReservaException.class, () -> reservaService.criar(reservaValida));
    }

    @Test
    void criar_DeveLancarValidacaoExceptionSeDataInicioPassada() {
        reservaValida.setDataInicioReserva(hoje.minusDays(1)); // Data no passado

        assertThrows(ValidacaoReservaException.class, () -> reservaService.criar(reservaValida));
    }

    @Test
    void criar_DeveLancarValidacaoExceptionSeQuartoIndisponivel() {
        // Simula um conflito: o findBy retorna uma reserva existente
        when(reservaRepository.findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(new Reserva()));

        assertThrows(ValidacaoReservaException.class, () -> reservaService.criar(reservaValida));
    }

    @Test
    void atualizar_DeveLancarValidacaoExceptionSeHouverConflitoComOutraReserva() {
        Reserva detalhesAtualizados = new Reserva();
        detalhesAtualizados.setNumeroDoQuarto("101");
        detalhesAtualizados.setDataInicioReserva(amanha.plusDays(5));
        detalhesAtualizados.setDataFinalReserva(amanha.plusDays(7));
        detalhesAtualizados.setHospedeId(10L);
        
        // Reserva 2 é o conflito
        Reserva reservaConflito = new Reserva();
        reservaConflito.setId(2L); 

        // 1. Simula a busca da reserva que está sendo atualizada
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaValida));

        // 2. Simula o retorno de um conflito (uma reserva com ID diferente)
        when(reservaRepository.findByNumeroDoQuartoAndDataFinalReservaAfterAndDataInicioReservaBefore(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(reservaValida, reservaConflito)); // Retorna a própria + um conflito

        assertThrows(ValidacaoReservaException.class, () -> reservaService.atualizar(1L, detalhesAtualizados));
    }
}
