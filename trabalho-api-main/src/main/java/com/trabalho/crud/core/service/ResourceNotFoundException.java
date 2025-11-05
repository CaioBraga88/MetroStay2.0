package com.trabalho.crud.core.service;

/**
 * Exceção lançada quando um recurso (como uma Reserva) não é encontrado pelo ID.
 * Estende RuntimeException para ser uma exceção não-checada (unchecked), o que é
 * ideal para uso com o método Optional.orElseThrow().
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Construtor que aceita a mensagem detalhada. No contexto da ReservaService,
     * esta mensagem geralmente informa que a Reserva com o ID X não foi encontrada.
     * * @param mensagem A descrição do erro.
     */
    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}
