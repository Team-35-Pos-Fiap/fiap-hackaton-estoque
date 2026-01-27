package br.com.fiap.estoque_service.entities.record.response;

public record ErrorResponse(
    int status,
    String erro,
    String mensagem
) {}
