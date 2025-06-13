package com.mobiauto.backendpaulo.dto;

import com.mobiauto.backendpaulo.entity.Revenda;
import com.mobiauto.backendpaulo.entity.Usuario;

public record UsuarioDTO(Long id, String nome, String senha, String email, PerfilUsuario perfilUsuario, Long revendaID) {

    public UsuarioDTO(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getSenha(), usuario.getEmail(), usuario.getPerfilUsuario(), usuario.getRevenda().getId());
    }
}
