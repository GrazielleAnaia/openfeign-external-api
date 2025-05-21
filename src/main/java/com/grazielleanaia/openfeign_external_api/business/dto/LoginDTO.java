package com.grazielleanaia.openfeign_external_api.business.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginDTO {

    private String email;
    private String senha;


}
