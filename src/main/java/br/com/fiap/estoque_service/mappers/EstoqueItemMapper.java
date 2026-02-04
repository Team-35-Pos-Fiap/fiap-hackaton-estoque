package br.com.fiap.estoque_service.mappers;

import br.com.fiap.estoque_service.entities.domain.EstoqueItemDomain;
import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import br.com.fiap.estoque_service.dto.request.InsumoRequestDto;
import br.com.fiap.estoque_service.dto.response.InsumoPaginacaoResponseDto;
import br.com.fiap.estoque_service.dto.response.InsumoResponseDto;
import br.com.fiap.estoque_service.dto.response.PaginacaoResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public class EstoqueItemMapper {

    public static EstoqueItemDomain fromRecordToDomain(UUID idUnidade, InsumoRequestDto dadosIniciaisInsumo) {
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

    public static InsumoResponseDto fromDomainToResponse(EstoqueItemDomain estoqueItemDomain){
        return new InsumoResponseDto(
            estoqueItemDomain.getIdUnidade(),
            estoqueItemDomain.getIdInsumo(),
            estoqueItemDomain.getQuantidadeDisponivel()
        );
    }

    public static InsumoPaginacaoResponseDto fromModelToResponsePaginated(Page<EstoqueItemModel> dados){
        List<InsumoResponseDto> insumos = dados.toList()
            .stream()
            .map(EstoqueItemMapper::fromModelToDomain)
            .map(EstoqueItemMapper::fromDomainToResponse)
            .toList();

        PaginacaoResponseDto dadosPaginacao = new PaginacaoResponseDto(dados.getNumber() + 1, dados.getTotalPages(), Long.valueOf(dados.getTotalElements()).intValue());

        return new InsumoPaginacaoResponseDto(insumos, dadosPaginacao);
    }
}
