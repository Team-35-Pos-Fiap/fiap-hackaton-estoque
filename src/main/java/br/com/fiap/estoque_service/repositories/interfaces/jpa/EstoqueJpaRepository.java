package br.com.fiap.estoque_service.repositories.interfaces.jpa;

import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EstoqueJpaRepository extends JpaRepository<EstoqueItemModel, UUID> {
    boolean existsByIdUnidadeAndIdInsumo(UUID idUnidade, UUID idInsumo);
    Optional<EstoqueItemModel> findByIdUnidadeAndIdInsumo(UUID idUnidade, UUID idInsumo);
    Page<EstoqueItemModel> findAllByIdUnidade(UUID idUnidade, Pageable pageable);
    Page<EstoqueItemModel> findAllByIdInsumo(UUID idInsumo, Pageable pageable);
}
