package br.com.fiap.estoque_service.exceptions;

public class InsumoJaCadastradoNaUnidadeException extends RuntimeException {
    public InsumoJaCadastradoNaUnidadeException() {
        super("Insumo já cadastrado na unidade.");
    }

    public InsumoJaCadastradoNaUnidadeException(String message) {
        super(message);
    }
}
