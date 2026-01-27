package br.com.fiap.estoque_service.entities.record.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InsumoRecordRequest(
    @NotNull(message = "o campo id do insumo precisa ser informado.")
    UUID idInsumo,
    Integer quantidadeInicial
) { }
