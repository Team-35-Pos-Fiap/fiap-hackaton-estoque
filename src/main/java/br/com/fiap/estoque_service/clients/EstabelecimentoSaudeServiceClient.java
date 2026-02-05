package br.com.fiap.estoque_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "estabelecimento-saude",
    url = "${services.estabelecimento-saude:}"
)
public interface EstabelecimentoSaudeServiceClient{
    @GetMapping("/estabelecimento-saude/estabelecimentos/{idEstabelecimento}")
    void buscarEstabelecimentoPorId(@PathVariable UUID idEstabelecimento);

}

