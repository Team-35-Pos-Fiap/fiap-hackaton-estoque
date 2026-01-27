package br.com.fiap.estoque_service.mappers;

import br.com.fiap.estoque_service.entities.domain.EstoqueItemDomain;
import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import br.com.fiap.estoque_service.entities.record.request.InsumoRecordRequest;
import br.com.fiap.estoque_service.entities.record.response.InsumoRecordPaginacaoResponse;
import br.com.fiap.estoque_service.entities.record.response.InsumoRecordResponse;
import br.com.fiap.estoque_service.entities.record.response.PaginacaoRecordResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public class EstoqueItemMapper {

    public static EstoqueItemDomain fromRecordToDomain(UUID idUnidade, InsumoRecordRequest dadosIniciaisInsumo) {
        return new EstoqueItemDomain(
            null,
            dadosIniciaisInsumo.idInsumo(),
            idUnidade,
            dadosIniciaisInsumo.quantidadeInicial() != null ? dadosIniciaisInsumo.quantidadeInicial() : 0
        );
    }

    public static EstoqueItemModel fromDomainToModel(EstoqueItemDomain estoqueItemDomain){
        return new EstoqueItemModel(
            estoqueItemDomain.getId(),
            estoqueItemDomain.getIdInsumo(),
            estoqueItemDomain.getIdUnidade(),
            estoqueItemDomain.getQuantidadeDisponivel()
        );
    }

    public static EstoqueItemDomain fromModelToDomain(EstoqueItemModel estoqueItemModel){
        return new EstoqueItemDomain(
            estoqueItemModel.getId(),
            estoqueItemModel.getIdInsumo(),
            estoqueItemModel.getIdUnidade(),
            estoqueItemModel.getQuantidadeDisponivel()
        );
    }

    public static InsumoRecordResponse fromDomainToResponse(EstoqueItemDomain estoqueItemDomain){
        return new InsumoRecordResponse(
            estoqueItemDomain.getIdUnidade(),
            estoqueItemDomain.getIdInsumo(),
            estoqueItemDomain.getQuantidadeDisponivel()
        );
    }

    public static InsumoRecordPaginacaoResponse fromModelToResponsePaginated(Page<EstoqueItemModel> dados){
        List<InsumoRecordResponse> insumos = dados.toList()
            .stream()
            .map(EstoqueItemMapper::fromModelToDomain)
            .map(EstoqueItemMapper::fromDomainToResponse)
            .toList();

        PaginacaoRecordResponse dadosPaginacao = new PaginacaoRecordResponse(dados.getNumber() + 1, dados.getTotalPages(), Long.valueOf(dados.getTotalElements()).intValue());

        return new InsumoRecordPaginacaoResponse(insumos, dadosPaginacao);
    }
}
