package br.com.fiap.estoque_service.dto.request;

import br.com.fiap.estoque_service.entities.enums.TipoDeMovimentacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record MovimentacaoRequestDto(
    @NotNull @Valid TipoDeMovimentacao tipo,
    @NotNull @Positive Integer quantidade,
    // Opcional, necessário so se o tipo de transferência for movimentação.
    // Verificação no service
    UUID idUnidadeDestino
) {
}
