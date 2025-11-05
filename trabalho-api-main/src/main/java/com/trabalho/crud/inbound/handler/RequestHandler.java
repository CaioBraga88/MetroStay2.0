package com.trabalho.crud.inbound.handler;

import com.trabalho.crud.core.service.ValidacaoReservaException;
import com.trabalho.crud.core.service.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RequestHandler {

    // -------------------------------------------------------------------------
    // 1. Tratamento para Exceção de Regra de Negócio (400 Bad Request)
    // Captura ValidacaoReservaException e retorna 400.
    // -------------------------------------------------------------------------
    @ExceptionHandler(ValidacaoReservaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidacaoReservaException(
            ValidacaoReservaException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request - Erro de Validação");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Retorna o status 400 (Bad Request) com a mensagem de erro da regra de negócio.
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    // -------------------------------------------------------------------------
    // 2. Tratamento para Recurso Não Encontrado (404 Not Found)
    // Captura ResourceNotFoundException e retorna 404.
    // -------------------------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found - Recurso Não Encontrado");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Retorna o status 404 (Not Found) com a mensagem de erro.
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
	
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocorreu um erro interno inesperado.");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
