package br.com.fiap.estoque_service.dto.response;

import java.util.List;

public record InsumoPaginacaoResponseDto(
    List<InsumoResponseDto> insumos,
    PaginacaoResponseDto dadosPaginacao
) {
}
