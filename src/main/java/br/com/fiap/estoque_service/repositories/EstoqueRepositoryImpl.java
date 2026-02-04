package br.com.fiap.estoque_service.repositories;

import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import br.com.fiap.estoque_service.exceptions.InsumoNaoCadastradoNaUnidadeException;
import br.com.fiap.estoque_service.exceptions.PaginaInvalidaException;
import br.com.fiap.estoque_service.repositories.interfaces.jpa.EstoqueJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EstoqueRepositoryImpl implements br.com.fiap.estoque_service.repositories.interfaces.EstoqueRepository {

    private final EstoqueJpaRepository estoqueRepository;
    private static final int QUANTIDADE_REGISTROS = 5;

    public EstoqueRepositoryImpl(EstoqueJpaRepository estoqueRepository) {
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
    public EstoqueItemModel buscarPorUnidadeEInsumo(UUID idUnidade, UUID idInsumo) {
        return getEstoqueItemModel(estoqueRepository.findByIdUnidadeAndIdInsumo(idUnidade, idInsumo));
    }

    @Override
    public void deletar(EstoqueItemModel estoqueItemModel) {
        estoqueRepository.delete(estoqueItemModel);
    }

    @Override
    public Page<EstoqueItemModel> buscarPorUnidadePaginado(UUID idUnidade, Integer pagina) {
        if (pagina == null || pagina < 1) {
            throw new PaginaInvalidaException("O número da página deve ser maior ou igual a 1.");
        }

        return estoqueRepository.findAllByIdUnidade(idUnidade, PageRequest.of(pagina - 1, QUANTIDADE_REGISTROS));
    }

    @Override
    public Page<EstoqueItemModel> buscarPorInsumoPaginado(UUID idInsumo, Integer pagina) {
        if (pagina == null || pagina < 1) {
            throw new PaginaInvalidaException("O número da página deve ser maior ou igual a 1.");
        }

        return estoqueRepository.findAllByIdInsumo(idInsumo, PageRequest.of(pagina - 1, QUANTIDADE_REGISTROS));
    }

    private EstoqueItemModel getEstoqueItemModel(Optional<EstoqueItemModel> dadosEstoqueItem){
        return dadosEstoqueItem.orElseThrow(InsumoNaoCadastradoNaUnidadeException::new);
    }
}
