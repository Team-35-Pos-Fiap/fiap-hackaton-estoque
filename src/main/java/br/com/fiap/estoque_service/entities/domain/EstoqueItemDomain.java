package br.com.fiap.estoque_service.entities.domain;

import br.com.fiap.estoque_service.exceptions.EstoqueInsuficienteException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class EstoqueItemDomain {

    // Getters
    private final UUID id;
    private final UUID idInsumo;
    private final UUID idUnidade;
    private int quantidadeDisponivel;

    public EstoqueItemDomain(UUID id, UUID idInsumo, UUID idUnidade, int quantidadeDisponivel) {
        if (quantidadeDisponivel < 0) {
            throw new IllegalArgumentException("Quantidade disponível não pode ser negativa");
        }
        this.id = id;
        this.idInsumo = idInsumo;
        this.idUnidade = idUnidade;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void entradaDeEstoque(int quantidadeDeEntrada) {
        if (quantidadeDeEntrada <= 0) throw new IllegalArgumentException("Quantidade de entrada deve ser > 0");
        this.quantidadeDisponivel += quantidadeDeEntrada;
    }

    public void saidaDeEstoque(int quantidadeDeSaida) {
        if (quantidadeDeSaida <= 0) throw new IllegalArgumentException("Quantidade de saída deve ser > 0");
        if (quantidadeDisponivel - quantidadeDeSaida < 0) {
            throw new EstoqueInsuficienteException("Estoque insuficiente");
        }
        this.quantidadeDisponivel -= quantidadeDeSaida;
    }

}
