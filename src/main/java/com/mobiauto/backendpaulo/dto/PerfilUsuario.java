package com.mobiauto.backendpaulo.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum PerfilUsuario {
    PROPRIETARIO,
    ADMINISTRADOR,
    GERENTE,
    ASSISTENTE;

}
