package br.com.fiap.estoque_service.services.interfaces;

import br.com.fiap.estoque_service.dto.request.InsumoRequestDto;
import br.com.fiap.estoque_service.dto.request.MovimentacaoRequestDto;
import br.com.fiap.estoque_service.dto.response.InsumoPaginacaoResponseDto;
import br.com.fiap.estoque_service.dto.response.InsumoResponseDto;

import java.util.UUID;

public interface EstoqueService {
    InsumoResponseDto registrarInsumo(UUID idUnidade, InsumoRequestDto dadosIniciaisInsumo);
    void deletarInsumo(UUID idUnidade, UUID idInsumo);
    InsumoResponseDto movimentarInsumo(UUID idUnidade, UUID idInsumo, MovimentacaoRequestDto dadosMovimentacaoInsumo);
    InsumoResponseDto buscarInsumoPorId(UUID idUnidade, UUID idInsumo);
    InsumoPaginacaoResponseDto buscarTodosInsumos (UUID idUnidade, int pagina);
}
