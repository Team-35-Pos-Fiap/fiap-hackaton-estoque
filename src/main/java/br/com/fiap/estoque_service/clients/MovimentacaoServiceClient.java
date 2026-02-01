package br.com.fiap.estoque_service.clients;

import br.com.fiap.estoque_service.clients.record.request.MovimentacaoRequest;
import br.com.fiap.estoque_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "movimentacao-service",
    url = "${services.movimentacao:}",
    configuration = FeignConfig.class
)
public interface MovimentacaoServiceClient {
    @PostMapping("/movimentacoes")
    void registrar(@RequestBody MovimentacaoRequest request);
}
