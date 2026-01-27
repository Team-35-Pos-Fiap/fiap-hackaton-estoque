package br.com.fiap.estoque_service.clients.record.request;

import java.util.UUID;

public record MovimentacaoRequest(
    String tipoMovimentacao,
    UUID idInsumo,
    UUID idUnidadeOrigem,
    UUID idUnidadeDestino,
    Integer quantidade
) {}
