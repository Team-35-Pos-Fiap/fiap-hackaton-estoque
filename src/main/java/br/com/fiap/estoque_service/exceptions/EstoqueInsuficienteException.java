package br.com.fiap.estoque_service.exceptions;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException() {
        super("Estoque insuficiente");
    }

    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
