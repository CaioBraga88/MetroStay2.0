package com.trabalho.crud.inbound.handler;

import com.trabalho.crud.core.service.ResourceNotFoundException;
import com.trabalho.crud.core.service.ValidacaoReservaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para o RequestHandler (Manipulador Global de Exceções).
 * O objetivo é garantir que, para cada tipo de exceção lançada na aplicação, 
 * o handler retorne a resposta HTTP (ResponseEntity) correta com o status e o corpo esperados (Map).
 * * Correção aplicada para alinhar com a assinatura dos métodos do RequestHandler de produção:
 * - Uso de Mocks para simular WebRequest.
 * - Checagem do tipo de retorno (ResponseEntity<Object>) e do corpo (Map<String, Object>).
 */
public class RequestHandlerTest {

    private RequestHandler requestHandler;

    @Mock
    private WebRequest webRequest;

    // Constante para simular o path que o WebRequest retorna
    private static final String MOCK_PATH = "uri=/api/v1/reservas";
    private static final String EXPECTED_PATH = "/api/v1/reservas";

    @BeforeEach
    void setUp() {
        // Inicializa Mocks
        MockitoAnnotations.openMocks(this);
        requestHandler = new RequestHandler();

        // Configuração padrão do mock para simular o path de requisição
        when(webRequest.getDescription(false)).thenReturn(MOCK_PATH);
    }

    // -------------------------------------------------------------------------
    // TESTE PARA EXCEÇÕES DE VALIDAÇÃO (BAD_REQUEST - 400)
    // Cobre o método handleValidacaoReservaException.
    // -------------------------------------------------------------------------

    @Test
    void handleValidacaoReservaException_DeveRetornarBadRequest() {
        String mensagemErro = "A data final da reserva deve ser posterior à data de início.";
        ValidacaoReservaException exception = new ValidacaoReservaException(mensagemErro);

        // Chama o método do Handler com a exceção e o mock do WebRequest
        ResponseEntity<Object> response = requestHandler.handleValidacaoReservaException(exception, webRequest);

        // Verifica o Status HTTP
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        // Verifica o Corpo da Resposta (Map<String, Object>)
        @SuppressWarnings("unchecked") // Assumindo que o corpo é um Map, conforme RequestHandler
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);

        // Checagem dos campos
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Bad Request - Erro de Validação", body.get("error"));
        assertEquals(mensagemErro, body.get("message"));
        assertEquals(EXPECTED_PATH, body.get("path"));
    }
    
    // -------------------------------------------------------------------------
    // TESTE PARA EXCEÇÕES DE RECURSO NÃO ENCONTRADO (NOT_FOUND - 404)
    // Cobre o método handleResourceNotFoundException.
    // -------------------------------------------------------------------------

    @Test
    void handleResourceNotFoundException_DeveRetornarNotFound() {
        String mensagemErro = "Reserva com ID 99 não encontrada.";
        ResourceNotFoundException exception = new ResourceNotFoundException(mensagemErro);

        // Chama o método do Handler com a exceção e o mock do WebRequest
        ResponseEntity<Object> response = requestHandler.handleResourceNotFoundException(exception, webRequest);

        // Verifica o Status HTTP
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        // Verifica o Corpo da Resposta (Map<String, Object>)
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        
        // Checagem dos campos
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
        assertEquals("Not Found - Recurso Não Encontrado", body.get("error"));
        assertEquals(mensagemErro, body.get("message"));
        assertEquals(EXPECTED_PATH, body.get("path"));
    }

    // -------------------------------------------------------------------------
    // TESTE PARA EXCEÇÕES NÃO MAPEADAS (INTERNAL_SERVER_ERROR - 500)
    // Cobre o método handleAllExceptions.
    // -------------------------------------------------------------------------
    
    @Test
    void handleGenericException_DeveRetornarInternalServerError() {
        String mensagemErroGenerica = "Erro interno do servidor: Teste de erro genérico.";
        Exception exception = new Exception(mensagemErroGenerica);

        // Chama o método do Handler com a exceção e o mock do WebRequest
        ResponseEntity<Object> response = requestHandler.handleAllExceptions(exception, webRequest);

        // Verifica o Status HTTP
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        // Verifica o Corpo da Resposta (Map<String, Object>)
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);

        // Checagem dos campos (o handler de 500 usa uma mensagem fixa)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        // Seu RequestHandler usa uma mensagem fixa, não a mensagem da exceção
        assertEquals("Ocorreu um erro interno inesperado.", body.get("message")); 
        assertEquals(EXPECTED_PATH, body.get("path"));
    }
}
