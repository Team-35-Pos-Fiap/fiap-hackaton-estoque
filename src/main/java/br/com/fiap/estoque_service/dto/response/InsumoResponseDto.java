package br.com.fiap.estoque_service.dto.response;

import java.util.UUID;

public record InsumoResponseDto(
    UUID idUnidade,
    UUID idInsumo,
    Integer quantidade
) {
}
