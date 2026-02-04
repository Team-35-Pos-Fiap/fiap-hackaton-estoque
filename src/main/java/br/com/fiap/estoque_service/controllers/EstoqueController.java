package br.com.fiap.estoque_service.controllers;

import br.com.fiap.estoque_service.dto.request.InsumoRequestDto;
import br.com.fiap.estoque_service.dto.request.MovimentacaoRequestDto;
import br.com.fiap.estoque_service.dto.response.InsumoPaginacaoResponseDto;
import br.com.fiap.estoque_service.dto.response.InsumoResponseDto;
import br.com.fiap.estoque_service.services.interfaces.EstoqueService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/estoque")
@Slf4j
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/{idUnidade}/insumos")
    public ResponseEntity<InsumoResponseDto> registrarInsumo(@PathVariable UUID idUnidade, @Valid @RequestBody InsumoRequestDto dados){
        log.info("registrarInsumo(): id da unidade {} e dados do insumo {}", idUnidade, dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.registrarInsumo(idUnidade, dados));
    }

    @DeleteMapping("/{idUnidade}/insumos/{idInsumo}")
    public ResponseEntity<Void> deletarInsumo(@PathVariable UUID idUnidade, @PathVariable UUID idInsumo){
        log.info("deletarInsumo(): id da unidade {} e id do insumo {}", idUnidade, idInsumo);

        estoqueService.deletarInsumo(idUnidade, idInsumo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idUnidade}/insumos/{idInsumo}/movimentacao")
    public ResponseEntity<InsumoResponseDto> movimentarInsumo(@PathVariable UUID idUnidade, @PathVariable UUID idInsumo, @Valid @RequestBody MovimentacaoRequestDto dados){
        log.info("movimentarInsumo(): id da unidade {}, id do insumo {} e dados da movimentação {}", idUnidade, idInsumo, dados);

        return ResponseEntity.ok().body(estoqueService.movimentarInsumo(idUnidade, idInsumo, dados));
    }

    @GetMapping("/{idUnidade}/insumos/{idInsumo}")
    public ResponseEntity<InsumoResponseDto> buscarInsumoPorId(@PathVariable UUID idUnidade, @PathVariable UUID idInsumo){
        log.info("buscarInsumoPorId(): id da unidade {} e id do insumo {}", idUnidade, idInsumo);

        return ResponseEntity.ok().body(estoqueService.buscarInsumoPorId(idUnidade, idInsumo));
    }

    @GetMapping("/{idUnidade}/insumos")
    public ResponseEntity<InsumoPaginacaoResponseDto> buscarTodosInsumos(@PathVariable UUID idUnidade, @RequestParam(defaultValue = "1") final Integer pagina){
        log.info("buscarTodosInsumos(): id da unidade {} e página {}", idUnidade, pagina);

        return ResponseEntity.ok().body(estoqueService.buscarTodosInsumos(idUnidade, pagina));
    }
}
