package br.com.fiap.estoque_service.repositories;

import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import br.com.fiap.estoque_service.exceptions.InsumoNaoCadastradoNaUnidadeException;
import br.com.fiap.estoque_service.exceptions.PaginaInvalidaException;
import br.com.fiap.estoque_service.repositories.interfaces.IEstoqueRepository;
import br.com.fiap.estoque_service.repositories.interfaces.jpa.IEstoqueJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EstoqueRepository implements IEstoqueRepository {

    private final IEstoqueJpaRepository estoqueRepository;
    private static final int QUANTIDADE_REGISTROS = 5;

    public EstoqueRepository(IEstoqueJpaRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    @Override
    public boolean insumoJaCadastradoNaUnidade(UUID idUnidade, UUID idInsumo) {
        return estoqueRepository.existsByIdUnidadeAndIdInsumo(idUnidade, idInsumo);
    }

    @Override
    public void salvar(EstoqueItemModel estoqueItemModel) {
        estoqueRepository.save(estoqueItemModel);
    }

    @Override
    public EstoqueItemModel buscarPorIdUnidadeEIdInsumo(UUID idUnidade, UUID idInsumo) {
        return getEstoqueItemModel(estoqueRepository.findByIdUnidadeAndIdInsumo(idUnidade, idInsumo));
    }

    @Override
    public void deletar(EstoqueItemModel estoqueItemModel) {
        estoqueRepository.delete(estoqueItemModel);
    }

    @Override
    public Page<EstoqueItemModel> buscarInsumosPorUnidadePaginado(UUID idUnidade, Integer pagina) {
        if (pagina == null || pagina < 1) {
            throw new PaginaInvalidaException("O número da página deve ser maior ou igual a 1.");
        }

        return estoqueRepository.findAllByIdUnidade(idUnidade, PageRequest.of(pagina - 1, QUANTIDADE_REGISTROS));
    }

    private EstoqueItemModel getEstoqueItemModel(Optional<EstoqueItemModel> dadosEstoqueItem){
        return dadosEstoqueItem.orElseThrow(InsumoNaoCadastradoNaUnidadeException::new);
    }
}
