package com.grazielleanaia.openfeign_external_api.business;

import com.grazielleanaia.openfeign_external_api.infrastructure.client.ViaCepClient;
import com.grazielleanaia.openfeign_external_api.infrastructure.client.ViaCepDTO;
import com.grazielleanaia.openfeign_external_api.infrastructure.exception.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class ViaCepService {

    private final ViaCepClient viaCepClient;

    public ViaCepDTO buscaEnderecoViaCep(String cep) {

return viaCepClient.buscaEndereco(retornaCepFormatado(cep));
    }

    private String retornaCepFormatado(String cep) {
        String cepFormatado = cep.replace(" ", "").replace("-", "");

        if(!cepFormatado.matches("\\d+")|| !Objects.equals(cepFormatado.length(), 8)){
            throw new IllegalArgumentException("O cep contem caracteres invalidos");
        }
        return cepFormatado;
    }
}

