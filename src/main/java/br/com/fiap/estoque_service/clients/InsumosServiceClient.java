package br.com.fiap.estoque_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "insumos",
    url = "${services.insumos:}"
)
public interface InsumosServiceClient {
    @GetMapping("/insumos/{idInsumo}")
    void buscarInsumoPorId(@PathVariable UUID idInsumo);
}
