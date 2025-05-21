package com.grazielleanaia.openfeign_external_api.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "telefone")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class Telefone {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private String numero;


    @Column(name = "usuario_id")
    private Long usuario_id;

}
