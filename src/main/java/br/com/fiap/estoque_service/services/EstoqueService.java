package br.com.fiap.estoque_service.services;

import br.com.fiap.estoque_service.clients.MovimentacaoServiceClient;
import br.com.fiap.estoque_service.clients.record.request.MovimentacaoRequest;
import br.com.fiap.estoque_service.entities.domain.EstoqueItemDomain;
import br.com.fiap.estoque_service.entities.enums.TipoDeMovimentacao;
import br.com.fiap.estoque_service.entities.model.EstoqueItemModel;
import br.com.fiap.estoque_service.entities.record.request.InsumoRecordRequest;
import br.com.fiap.estoque_service.entities.record.request.MovimentacaoInsumoRecordRequest;
import br.com.fiap.estoque_service.entities.record.response.InsumoRecordPaginacaoResponse;
import br.com.fiap.estoque_service.entities.record.response.InsumoRecordResponse;
import br.com.fiap.estoque_service.exceptions.InsumoComQuantidadeMaiorDoQueZeroException;
import br.com.fiap.estoque_service.exceptions.InsumoJaCadastradoNaUnidadeException;
import br.com.fiap.estoque_service.exceptions.InsumoNaoCadastradoNaUnidadeException;
import br.com.fiap.estoque_service.exceptions.TransferenciaInvalidaException;
import br.com.fiap.estoque_service.mappers.EstoqueItemMapper;
import br.com.fiap.estoque_service.repositories.interfaces.IEstoqueRepository;
import br.com.fiap.estoque_service.services.interfaces.IEstoqueService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EstoqueService implements IEstoqueService {

    private final IEstoqueRepository estoqueRepository;
    private final MovimentacaoServiceClient movimentacaoClient;

    public EstoqueService(IEstoqueRepository estoqueRepository, MovimentacaoServiceClient movimentacaoClient) {
        this.estoqueRepository = estoqueRepository;
        this.movimentacaoClient = movimentacaoClient;
    }

    @Override
    @Transactional
    public InsumoRecordResponse registrarInsumo(UUID idUnidade, InsumoRecordRequest request) {
        return criarItemEstoque(
            idUnidade,
            request.idInsumo(),
            request.quantidadeInicial()
        );
    }

    @Override
    public void deletarInsumo(UUID idUnidade, UUID idInsumo) {
        EstoqueItemModel model = buscarItemOuFalhar(idUnidade, idInsumo);

        if (model.getQuantidadeDisponivel() > 0) {
            throw new InsumoComQuantidadeMaiorDoQueZeroException(
                "Itens com estoque maior que zero não podem ser deletados"
            );
        }

        estoqueRepository.deletar(model);
    }

    @Override
    @Transactional
    public InsumoRecordResponse movimentarInsumo(
        UUID idUnidadeOrigem,
        UUID idInsumo,
        MovimentacaoInsumoRecordRequest request
    ) {
        validarTransferencia(request);

        return switch (request.tipo()) {
            case ENTRADA -> entradaDeInsumo(idUnidadeOrigem, idInsumo, request.quantidade());
            case SAIDA -> saidaDeInsumo(idUnidadeOrigem, idInsumo, request.quantidade());
            case PERDA -> perdaDeInsumo(idUnidadeOrigem, idInsumo, request.quantidade());
            case TRANSFERENCIA -> transferenciaDeInsumo(
                idUnidadeOrigem,
                idInsumo,
                request.quantidade(),
                request.idUnidadeDestino()
            );
        };
    }

    @Override
    public InsumoRecordResponse buscarInsumoPorId(UUID idUnidade, UUID idInsumo) {
        EstoqueItemDomain domain = buscarDomain(idUnidade, idInsumo);

        return EstoqueItemMapper.fromDomainToResponse(domain);
    }

    @Override
    public InsumoRecordPaginacaoResponse buscarTodosInsumos(UUID idUnidade, int pagina) {
        Page<EstoqueItemModel> insumos = estoqueRepository.buscarInsumosPorUnidadePaginado(idUnidade, pagina);

        return EstoqueItemMapper.fromModelToResponsePaginated(insumos);
    }

    /**
     * Cria um novo item de estoque na unidade informada.
     * Registra movimentação como ENTRADA.
     *
     * @param idUnidade ID da unidade
     * @param idInsumo ID do insumo
     * @param quantidade Quantidade inicial do estoque
     * @return InsumoRecordResponse criado
     * @throws InsumoJaCadastradoNaUnidadeException se o insumo já existe na unidade
     */
    private InsumoRecordResponse criarItemEstoque(UUID idUnidade, UUID idInsumo, int quantidade) {
        if (estoqueRepository.insumoJaCadastradoNaUnidade(idUnidade, idInsumo)) {
            throw new InsumoJaCadastradoNaUnidadeException("Insumo já cadastrado na unidade");
        }

        EstoqueItemDomain domain = new EstoqueItemDomain(null, idInsumo, idUnidade, quantidade);
        registrarMovimentacao(TipoDeMovimentacao.ENTRADA, idInsumo, idUnidade, null, quantidade);
        estoqueRepository.salvar(EstoqueItemMapper.fromDomainToModel(domain));
        return EstoqueItemMapper.fromDomainToResponse(domain);
    }

    /**
     * Realiza uma entrada de insumo.
     * Se o insumo não existir, cria novo item.
     * Caso contrário, incrementa estoque existente e registra movimentação.
     */
    private InsumoRecordResponse entradaDeInsumo(UUID idUnidade, UUID idInsumo, int quantidade) {
        if (!estoqueRepository.insumoJaCadastradoNaUnidade(idUnidade, idInsumo)) {
            return criarItemEstoque(idUnidade, idInsumo, quantidade);
        }

        EstoqueItemDomain domain = buscarDomain(idUnidade, idInsumo);
        domain.entradaDeEstoque(quantidade);
        registrarMovimentacao(TipoDeMovimentacao.ENTRADA, idInsumo, idUnidade, null, quantidade);
        estoqueRepository.salvar(EstoqueItemMapper.fromDomainToModel(domain));
        return EstoqueItemMapper.fromDomainToResponse(domain);
    }

    /**
     * Realiza uma saída de insumo.
     * Reduz estoque existente e registra movimentação como SAIDA.
     */
    private InsumoRecordResponse saidaDeInsumo(UUID idUnidade, UUID idInsumo, int quantidade) {
        EstoqueItemDomain domain = buscarDomain(idUnidade, idInsumo);
        domain.saidaDeEstoque(quantidade);
        registrarMovimentacao(TipoDeMovimentacao.SAIDA, idInsumo, idUnidade, null, quantidade);
        estoqueRepository.salvar(EstoqueItemMapper.fromDomainToModel(domain));
        return EstoqueItemMapper.fromDomainToResponse(domain);
    }

    /**
     * Realiza perda de insumo.
     * Reduz estoque existente e registra movimentação como PERDA.
     */
    private InsumoRecordResponse perdaDeInsumo(UUID idUnidade, UUID idInsumo, int quantidade) {
        EstoqueItemDomain domain = buscarDomain(idUnidade, idInsumo);
        domain.saidaDeEstoque(quantidade);
        registrarMovimentacao(TipoDeMovimentacao.PERDA, idInsumo, idUnidade, null, quantidade);
        estoqueRepository.salvar(EstoqueItemMapper.fromDomainToModel(domain));
        return EstoqueItemMapper.fromDomainToResponse(domain);
    }

    /**
     * Realiza transferência de insumo entre unidades.
     * Executa saída na unidade de origem e entrada na unidade destino.
     * Registra movimentação como TRANSFERENCIA.
     */
    private InsumoRecordResponse transferenciaDeInsumo(UUID idUnidadeOrigem, UUID idInsumo, int quantidade, UUID idUnidadeDestino) {
        // saída da origem
        EstoqueItemDomain origemDomain = buscarDomain(idUnidadeOrigem, idInsumo);
        origemDomain.saidaDeEstoque(quantidade);
        estoqueRepository.salvar(EstoqueItemMapper.fromDomainToModel(origemDomain));

        // entrada no destino
        EstoqueItemDomain destinoDomain;
        if (estoqueRepository.insumoJaCadastradoNaUnidade(idUnidadeDestino, idInsumo)) {
            destinoDomain = buscarDomain(idUnidadeDestino, idInsumo);
            destinoDomain.entradaDeEstoque(quantidade);
        } else {
            destinoDomain = new EstoqueItemDomain(null, idInsumo, idUnidadeDestino, quantidade);
        }
        estoqueRepository.salvar(EstoqueItemMapper.fromDomainToModel(destinoDomain));

        // registrar movimentacao via Feign
        registrarMovimentacao(TipoDeMovimentacao.TRANSFERENCIA, idInsumo, idUnidadeOrigem, idUnidadeDestino, quantidade);

        return EstoqueItemMapper.fromDomainToResponse(destinoDomain);
    }

    /** Busca domínio de um insumo em uma unidade, lança exceção se não encontrado */
    private EstoqueItemDomain buscarDomain(UUID idUnidade, UUID idInsumo) {
        return EstoqueItemMapper.fromModelToDomain(buscarItemOuFalhar(idUnidade, idInsumo));
    }

    /** Busca modelo de insumo ou lança exceção se não existir na unidade */
    private EstoqueItemModel buscarItemOuFalhar(UUID idUnidade, UUID idInsumo) {
        if (!estoqueRepository.insumoJaCadastradoNaUnidade(idUnidade, idInsumo)) {
            throw new InsumoNaoCadastradoNaUnidadeException("Insumo não cadastrado na unidade");
        }
        return estoqueRepository.buscarPorIdUnidadeEIdInsumo(idUnidade, idInsumo);
    }

    /** Valida dados da transferência, exigindo unidade destino */
    private void validarTransferencia(MovimentacaoInsumoRecordRequest request) {
        if (request.tipo() == TipoDeMovimentacao.TRANSFERENCIA && request.idUnidadeDestino() == null) {
            throw new TransferenciaInvalidaException("idUnidadeDestino é obrigatório para TRANSFERENCIA");
        }
    }

    /**
     * Registra movimentação via Feign para o serviço de movimentacao.
     *
     * @param tipo tipo da movimentação (ENTRADA, SAIDA, PERDA, TRANSFERENCIA)
     * @param idInsumo ID do insumo
     * @param origem ID da unidade origem
     * @param destino ID da unidade destino (para transferências)
     * @param quantidade quantidade movimentada
     */
    private void registrarMovimentacao(TipoDeMovimentacao tipo, UUID idInsumo, UUID origem, UUID destino, int quantidade) {
        movimentacaoClient.registrar(new MovimentacaoRequest(tipo.name(), idInsumo, origem, destino, quantidade));
    }
}
