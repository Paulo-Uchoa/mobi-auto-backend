package com.mobiauto.backendpaulo.entity;

import com.mobiauto.backendpaulo.dto.PerfilUsuario;
import com.mobiauto.backendpaulo.dto.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfilUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    private Revenda revenda;


    public Usuario(UsuarioDTO usuario, Revenda revenda) {
        this.nome = usuario.nome();
        this.email = usuario.email();
        this.perfilUsuario = usuario.perfilUsuario();
        this.revenda = revenda;
    }

    public Usuario(UsuarioDTO usuario) {
        this.nome = usuario.nome();
        this.email = usuario.email();
        this.perfilUsuario = usuario.perfilUsuario();
    }

    public boolean isAdmintrador() {
        return this.perfilUsuario == PerfilUsuario.ADMINISTRADOR;
    }

    public boolean isGerenteOrProprietario() {
        return this.perfilUsuario == PerfilUsuario.GERENTE || this.perfilUsuario == PerfilUsuario.PROPRIETARIO;
    }
}
