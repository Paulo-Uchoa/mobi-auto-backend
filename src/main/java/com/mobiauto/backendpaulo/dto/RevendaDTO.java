package com.mobiauto.backendpaulo.dto;

import com.mobiauto.backendpaulo.entity.Revenda;
import com.mobiauto.backendpaulo.entity.Usuario;

public record RevendaDTO(Long id, String cnpj, String nomeSocial) {

    public RevendaDTO(Revenda revenda){
        this(revenda.getId(), revenda.getCnpj(), revenda.getNomeSocial());
    }
}
