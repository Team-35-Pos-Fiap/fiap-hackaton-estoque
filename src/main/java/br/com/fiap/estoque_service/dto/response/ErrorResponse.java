package br.com.fiap.estoque_service.dto.response;

public record ErrorResponse(
    int status,
    String erro,
    String mensagem
) {}
