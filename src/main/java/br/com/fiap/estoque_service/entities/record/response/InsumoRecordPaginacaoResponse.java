package br.com.fiap.estoque_service.entities.record.response;

import java.util.List;

public record InsumoRecordPaginacaoResponse(
    List<InsumoRecordResponse> insumos,
    PaginacaoRecordResponse dadosPaginacao
) {
}
