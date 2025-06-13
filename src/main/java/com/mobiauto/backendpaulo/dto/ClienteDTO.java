package com.mobiauto.backendpaulo.dto;

import com.mobiauto.backendpaulo.entity.Cliente;

public record ClienteDTO(String nome, String email, String telefone) {

    public ClienteDTO(Cliente cliente){
        this(cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}
