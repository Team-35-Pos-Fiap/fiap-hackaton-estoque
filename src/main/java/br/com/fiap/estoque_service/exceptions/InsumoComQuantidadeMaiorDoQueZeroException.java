package br.com.fiap.estoque_service.exceptions;

public class InsumoComQuantidadeMaiorDoQueZeroException extends RuntimeException {
    public InsumoComQuantidadeMaiorDoQueZeroException() {
        super("Items com estoque maior do que zero não podem ser deletados");
    }

  public InsumoComQuantidadeMaiorDoQueZeroException(String message) {
    super(message);
  }
}
