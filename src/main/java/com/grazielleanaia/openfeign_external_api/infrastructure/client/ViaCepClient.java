package com.grazielleanaia.openfeign_external_api.infrastructure.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url= "${via-cep.url}")

public interface ViaCepClient {

    @GetMapping("/ws/{cep}/json/")
    ViaCepDTO buscaEndereco(@PathVariable("cep") String cep);
}
