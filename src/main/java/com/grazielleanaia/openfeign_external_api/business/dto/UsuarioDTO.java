package com.grazielleanaia.openfeign_external_api.business.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;

    private List<EnderecoDTO> enderecos;

    private List<TelefoneDTO> telefones;

}
