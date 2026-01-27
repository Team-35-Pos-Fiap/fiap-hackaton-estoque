package br.com.fiap.estoque_service.exceptions;

public class InsumoNaoCadastradoNaUnidadeException extends RuntimeException {
    public InsumoNaoCadastradoNaUnidadeException() {
        super("Insumo não está cadastrado na unidade.");
    }

    public InsumoNaoCadastradoNaUnidadeException(String message) {
        super(message);
    }
}
