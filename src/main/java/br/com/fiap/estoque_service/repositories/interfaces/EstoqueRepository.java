package br.com.fiap.estoque_service.repositories.interfaces;

import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface EstoqueRepository {
    boolean insumoJaCadastradoNaUnidade(UUID idUnidade, UUID idInsumo);
    void salvar(EstoqueItemModel estoqueItemModel);
    EstoqueItemModel buscarPorIdUnidadeEIdInsumo(UUID idUnidade, UUID idInsumo);
    void deletar(EstoqueItemModel estoqueItemModel);
    Page<EstoqueItemModel> buscarInsumosPorUnidadePaginado(UUID idUnidade, Integer pagina);
}
