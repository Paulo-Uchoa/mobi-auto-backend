package com.mobiauto.backendpaulo.controller;

import com.mobiauto.backendpaulo.config.JwtConfig;
import com.mobiauto.backendpaulo.dto.LoginDTO;
import com.mobiauto.backendpaulo.dto.OportunidadeDTO;
import com.mobiauto.backendpaulo.dto.Status;
import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.repository.UsuarioRepository;
import com.mobiauto.backendpaulo.service.OportunidadeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Tag(name = "Login", description = "Método de Login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final UsuarioRepository usuarioRepository;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtConfig jwtConfig,
                           UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        try {

            Usuario usuario = usuarioRepository.findByEmail(dto.email())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            String token = jwtConfig.generateToken(usuario);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", usuario.getEmail(),
                    "perfil", usuario.getPerfilUsuario().name()
            ));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }
}
