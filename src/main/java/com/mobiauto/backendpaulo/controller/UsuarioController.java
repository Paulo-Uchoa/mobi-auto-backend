package com.mobiauto.backendpaulo.controller;

import com.mobiauto.backendpaulo.dto.PerfilUsuario;
import com.mobiauto.backendpaulo.dto.UsuarioDTO;
import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.exception.AcessoNegadoException;
import com.mobiauto.backendpaulo.exception.EmailCadastradoException;
import com.mobiauto.backendpaulo.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Usuarios", description = "Métodos dos Usuarioss")
@RequestMapping(value = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'PROPRIETARIO')")
    public ResponseEntity<UsuarioDTO> create(
            @RequestBody UsuarioDTO usuarioDTO,
            UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailLogado = userDetails.getUsername();
        Usuario logado = usuarioService.getUsuarioLogado(emailLogado);

        if (logado.getPerfilUsuario() != PerfilUsuario.ADMINISTRADOR &&
                !logado.getRevenda().getId().equals(usuarioDTO.revendaID())) {
            throw new AcessoNegadoException("Você só pode cadastrar usuários em sua própria loja.");
        }

        if(this.usuarioService.checkExistsEmail(usuarioDTO.email())){
            throw new EmailCadastradoException("Já existe um usuário cadastrado com este e-mail.");
        }

        UsuarioDTO response = this.usuarioService.create(usuarioDTO);

        URI uri = uriBuilder.path("/usuarios/{userID}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("")
    ResponseEntity<List<UsuarioDTO>> list(){
        return ResponseEntity.ok(this.usuarioService.list());
    }

    @GetMapping("/{userID}")
    ResponseEntity<UsuarioDTO> getByID(@PathVariable Long userID){
        return ResponseEntity.ok(this.usuarioService.getByID(userID));
    }


}
