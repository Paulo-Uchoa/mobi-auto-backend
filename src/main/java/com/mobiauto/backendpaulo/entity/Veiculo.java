package com.mobiauto.backendpaulo.entity;

import com.mobiauto.backendpaulo.dto.PerfilUsuario;
import com.mobiauto.backendpaulo.dto.VeiculoDTO;
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
public class Veiculo {

    private String marca;
    private String modelo;
    private String versao;
    private String anoModelo;

    public Veiculo(VeiculoDTO veiculo){
        this.marca = veiculo.marca();
        this.modelo = veiculo.modelo();
        this.versao = veiculo.versao();
        this.anoModelo = veiculo.anoModelo();
    }



}
