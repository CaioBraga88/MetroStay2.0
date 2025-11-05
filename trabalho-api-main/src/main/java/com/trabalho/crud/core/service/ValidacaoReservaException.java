package com.trabalho.crud.core.service;

/**
 * Exceção lançada quando uma regra de negócio de reserva é violada.
 * Estende RuntimeException para ser uma exceção não-checada (unchecked),
 * simplificando o uso no Service.
 */
public class ValidacaoReservaException extends RuntimeException {

    /**
     * Construtor que aceita a mensagem detalhada do erro de validação.
     * * @param mensagem A descrição do erro de negócio que ocorreu.
     */
    public ValidacaoReservaException(String mensagem) {
        // Chama o construtor da classe pai (RuntimeException)
        super(mensagem);
    }
}
