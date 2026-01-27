package br.com.fiap.estoque_service.services.interfaces;

import br.com.fiap.estoque_service.entities.record.request.InsumoRecordRequest;
import br.com.fiap.estoque_service.entities.record.request.MovimentacaoInsumoRecordRequest;
import br.com.fiap.estoque_service.entities.record.response.InsumoRecordPaginacaoResponse;
import br.com.fiap.estoque_service.entities.record.response.InsumoRecordResponse;

import java.util.UUID;

public interface IEstoqueService {
    InsumoRecordResponse registrarInsumo(UUID idUnidade, InsumoRecordRequest dadosIniciaisInsumo);
    void deletarInsumo(UUID idUnidade, UUID idInsumo);
    InsumoRecordResponse movimentarInsumo(UUID idUnidade, UUID idInsumo, MovimentacaoInsumoRecordRequest dadosMovimentacaoInsumo);
    InsumoRecordResponse buscarInsumoPorId(UUID idUnidade, UUID idInsumo);
    InsumoRecordPaginacaoResponse buscarTodosInsumos (UUID idUnidade, int pagina);
}
