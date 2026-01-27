package br.com.fiap.estoque_service.entities.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "estoque_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstoqueItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="id_insumo", nullable = false)
    private UUID idInsumo;
    @Column(name="id_unidade", nullable = false)
    private UUID idUnidade;
    /**
     * Quantidade disponível do insumo nesta unidade.
     */
    @Column(name="quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;
}
