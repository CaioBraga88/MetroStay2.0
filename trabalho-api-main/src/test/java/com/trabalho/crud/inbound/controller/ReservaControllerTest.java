package com.trabalho.crud.inbound.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabalho.crud.core.dto.ReservaDto;
import com.trabalho.crud.core.entity.Reserva;
import com.trabalho.crud.core.mapper.ReservaMapper;
import com.trabalho.crud.core.service.ResourceNotFoundException;
import com.trabalho.crud.core.service.ReservaService;
import com.trabalho.crud.core.service.ValidacaoReservaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o ReservaController usando MockMvc.
 * Garante que os endpoints da API funcionem corretamente e que o tratamento de exceções (RequestHandler) seja ativado.
 */
@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    // Simula as chamadas HTTP (POST, GET, etc.) sem subir o servidor completo
    @Autowired
    private MockMvc mockMvc;

    // Converte objetos Java em JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    // Simula a camada de Serviço (aqui onde injetamos os comportamentos desejados)
    @MockBean
    private ReservaService reservaService;

    // Simula a camada de Mapeamento
    @MockBean
    private ReservaMapper reservaMapper;

    private Reserva reservaMock;
    private ReservaDto reservaDtoMock;

    private static final String API_URL = "/api/v1/reservas";

    @BeforeEach
    void setUp() {
        // Objeto Reserva de exemplo
        reservaMock = new Reserva();
        reservaMock.setId(1L);
        reservaMock.setNumeroDoQuarto("101");
        reservaMock.setDataInicioReserva(LocalDate.now().plusDays(5));
        reservaMock.setDataFinalReserva(LocalDate.now().plusDays(10));
        reservaMock.setHospedeId(10L);

        // Objeto DTO de exemplo
        reservaDtoMock = new ReservaDto();
        reservaDtoMock.setId(1L);
        reservaDtoMock.setNumeroDoQuarto("101");
        reservaDtoMock.setDataInicioReserva(LocalDate.now().plusDays(5));
        reservaDtoMock.setDataFinalReserva(LocalDate.now().plusDays(10));
        reservaDtoMock.setHospedeId(10L);
    }

    // -------------------------------------------------------------------------
    // TESTES POST - CRIAR RESERVA
    // -------------------------------------------------------------------------

    @Test
    void criarReserva_DeveRetornarCreated() throws Exception {
        // Comportamento do Mocks
        when(reservaMapper.toEntity(any(ReservaDto.class))).thenReturn(reservaMock);
        when(reservaService.criar(any(Reserva.class))).thenReturn(reservaMock);
        when(reservaMapper.toDto(any(Reserva.class))).thenReturn(reservaDtoMock);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDtoMock)))
                .andExpect(status().isCreated()) // Espera HTTP 201
                .andExpect(jsonPath("$.id").value(1L)); // Verifica o ID no JSON de retorno
    }

    @Test
    void criarReserva_DeveRetornarBadRequest_EmValidacaoReservaException() throws Exception {
        // Comportamento do Mocks
        when(reservaMapper.toEntity(any(ReservaDto.class))).thenReturn(reservaMock);
        // Simula a falha de validação de negócio (Ex: datas inválidas)
        when(reservaService.criar(any(Reserva.class))).thenThrow(new ValidacaoReservaException("Datas inválidas para a reserva."));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDtoMock)))
                .andExpect(status().isBadRequest()) // Espera HTTP 400 (Tratado pelo RequestHandler)
                .andExpect(jsonPath("$.message").value("Datas inválidas para a reserva."));
    }

    // -------------------------------------------------------------------------
    // TESTES GET - BUSCAR RESERVAS
    // -------------------------------------------------------------------------

    @Test
    void buscarReservaPorId_DeveRetornarOk() throws Exception {
        // Comportamento do Mocks
        when(reservaService.buscarPorId(1L)).thenReturn(reservaMock);
        when(reservaMapper.toDto(any(Reserva.class))).thenReturn(reservaDtoMock);

        mockMvc.perform(get(API_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(jsonPath("$.numeroDoQuarto").value("101"));
    }

    @Test
    void buscarReservaPorId_DeveRetornarNotFound() throws Exception {
        // Simula o erro: Reserva não encontrada
        when(reservaService.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Reserva não encontrada com ID: 99"));

        mockMvc.perform(get(API_URL + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Espera HTTP 404 (Tratado pelo RequestHandler)
                .andExpect(jsonPath("$.message").value("Reserva não encontrada com ID: 99"));
    }

    @Test
    void buscarTodasReservas_DeveRetornarListaVazia() throws Exception {
        // Comportamento do Mocks: Retorna lista vazia
        when(reservaService.buscarTodas()).thenReturn(List.of());
        when(reservaMapper.toDtoList(anyList())).thenReturn(List.of());

        mockMvc.perform(get(API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty()); // Verifica se a lista JSON está vazia
    }

    // -------------------------------------------------------------------------
    // TESTES PUT - ATUALIZAR RESERVA
    // -------------------------------------------------------------------------

    @Test
    void atualizarReserva_DeveRetornarOk() throws Exception {
        // Comportamento do Mocks
        when(reservaMapper.toEntity(any(ReservaDto.class))).thenReturn(reservaMock);
        when(reservaService.atualizar(eq(1L), any(Reserva.class))).thenReturn(reservaMock);
        when(reservaMapper.toDto(any(Reserva.class))).thenReturn(reservaDtoMock);

        mockMvc.perform(put(API_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDtoMock)))
                .andExpect(status().isOk()) // Espera HTTP 200
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void atualizarReserva_DeveRetornarNotFound() throws Exception {
        // Simula o erro: Reserva não encontrada
        when(reservaMapper.toEntity(any(ReservaDto.class))).thenReturn(reservaMock);
        when(reservaService.atualizar(eq(99L), any(Reserva.class))).thenThrow(new ResourceNotFoundException("Reserva para atualização não encontrada."));

        mockMvc.perform(put(API_URL + "/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDtoMock)))
                .andExpect(status().isNotFound()) // Espera HTTP 404
                .andExpect(jsonPath("$.message").value("Reserva para atualização não encontrada."));
    }

    // -------------------------------------------------------------------------
    // TESTES DELETE - DELETAR RESERVA
    // -------------------------------------------------------------------------

    @Test
    void deletarReserva_DeveRetornarNoContent() throws Exception {
        // Comportamento do Mocks: Simplesmente não faz nada, indicando sucesso
        doNothing().when(reservaService).deletar(1L);

        mockMvc.perform(delete(API_URL + "/{id}", 1L))
                .andExpect(status().isNoContent()); // Espera HTTP 204
    }

    @Test
    void deletarReserva_DeveRetornarNotFound() throws Exception {
        // Simula o erro: Tentativa de deletar ID inexistente
        doThrow(new ResourceNotFoundException("Reserva a ser deletada não encontrada.")).when(reservaService).deletar(99L);

        mockMvc.perform(delete(API_URL + "/{id}", 99L))
                .andExpect(status().isNotFound()) // Espera HTTP 404
                .andExpect(jsonPath("$.message").value("Reserva a ser deletada não encontrada."));
    }
}