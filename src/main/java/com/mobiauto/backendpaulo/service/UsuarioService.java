package com.mobiauto.backendpaulo.service;


import com.mobiauto.backendpaulo.dto.UsuarioDTO;
import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.repository.RevendaRepository;
import com.mobiauto.backendpaulo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RevendaRepository revendaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDTO create(UsuarioDTO usuario){
        Usuario toSave = new Usuario(usuario, this.revendaRepository.getReferenceById(usuario.revendaID()));
        toSave.setSenha(passwordEncoder.encode(usuario.senha()));

        return new UsuarioDTO(this.usuarioRepository.save(toSave));
    }

    public List<UsuarioDTO> list(){
        return this.usuarioRepository.findAll().stream().map(UsuarioDTO::new).toList();
    }

    public UsuarioDTO getByID(Long userID){
        Usuario usuario = this.usuarioRepository.findById(userID).orElseThrow(() ->
                new EntityNotFoundException("Não existe usuário com este ID"));

        return new UsuarioDTO(usuario);
    }

    public Usuario getUsuarioLogado(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public boolean checkExistsEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }
}
