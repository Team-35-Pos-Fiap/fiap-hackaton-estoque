package br.com.fiap.estoque_service.entities.record.response;

import java.util.UUID;

public record InsumoRecordResponse(
    UUID idUnidade,
    UUID idInsumo,
    Integer quantidade
) {
}
