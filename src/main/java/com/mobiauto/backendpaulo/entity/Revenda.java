package com.mobiauto.backendpaulo.entity;

import com.mobiauto.backendpaulo.dto.RevendaDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Revenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    @Column(nullable = false)
    private String nomeSocial;

    @OneToMany(mappedBy = "revenda")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "revenda")
    private List<Oportunidade> oportunidades;

    public Revenda(RevendaDTO revenda){
        this.cnpj = revenda.cnpj();
        this.nomeSocial = revenda.nomeSocial();
    }
}
