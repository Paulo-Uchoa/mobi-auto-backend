package com.mobiauto.backendpaulo.dto;

import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.entity.Veiculo;

public record VeiculoDTO(String marca, String modelo, String versao, String anoModelo) {

    public VeiculoDTO(Veiculo veiculo){
        this(veiculo.getMarca(), veiculo.getModelo(), veiculo.getVersao(), veiculo.getAnoModelo());
    }
}
