package br.com.fiap.estoque_service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record InsumoRequestDto(
    @NotNull(message = "o campo id do insumo precisa ser informado.")
    UUID idInsumo,
    Integer quantidadeInicial
) { }
