package br.com.fiap.estoque_service.exceptions;

public class TransferenciaInvalidaException extends RuntimeException {
    public TransferenciaInvalidaException(String message) {
        super(message);
    }
}
