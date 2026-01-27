package br.com.fiap.estoque_service.exceptions;

public class PaginaInvalidaException extends RuntimeException {
    public PaginaInvalidaException(String message) {
        super(message);
    }
}
