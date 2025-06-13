package com.mobiauto.backendpaulo.entity;

import com.mobiauto.backendpaulo.dto.ClienteDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;


    public Cliente(ClienteDTO cliente) {
        this.nome = cliente.nome();
        this.email = cliente.email();
        this.telefone = cliente.telefone();
    }
}
