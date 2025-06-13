package com.mobiauto.backendpaulo.config;


import com.mobiauto.backendpaulo.dto.PerfilUsuario;
import com.mobiauto.backendpaulo.entity.Revenda;
import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.repository.RevendaRepository;
import com.mobiauto.backendpaulo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSender {

    @Bean
    public CommandLineRunner seedDatabase(RevendaRepository revendaRepository,
                                          UsuarioRepository usuarioRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {

            if (revendaRepository.count() == 0) {
                Revenda revenda = new Revenda();
                revenda.setNomeSocial("Revenda São Paulo");
                revenda.setCnpj("12345678000195");
                revendaRepository.save(revenda);
            }

            Revenda revenda = revendaRepository.findAll().get(0);

            if (usuarioRepository.findByEmail("admin@email.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@email.com");
                admin.setSenha(passwordEncoder.encode("123"));
                admin.setPerfilUsuario(PerfilUsuario.ADMINISTRADOR);
                admin.setRevenda(revenda);
                usuarioRepository.save(admin);
            }

            if (usuarioRepository.findByEmail("paulo@email.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Paulo Gerente");
                admin.setEmail("paulo@email.com");
                admin.setSenha(passwordEncoder.encode("123"));
                admin.setPerfilUsuario(PerfilUsuario.GERENTE);
                admin.setRevenda(revenda);
                usuarioRepository.save(admin);
            }

            if (usuarioRepository.findByEmail("proprietario@email.com").isEmpty()) {
                Usuario proprietario = new Usuario();
                proprietario.setNome("Proprietário Loja");
                proprietario.setEmail("proprietario@email.com");
                proprietario.setSenha(passwordEncoder.encode("123"));
                proprietario.setPerfilUsuario(PerfilUsuario.PROPRIETARIO);
                proprietario.setRevenda(revenda);
                usuarioRepository.save(proprietario);
            }

            if (usuarioRepository.findByEmail("assistente@email.com").isEmpty()) {
                Usuario assistente = new Usuario();
                assistente.setNome("Assistente da Loja");
                assistente.setEmail("assistente@email.com");
                assistente.setSenha(passwordEncoder.encode("123"));
                assistente.setPerfilUsuario(PerfilUsuario.ASSISTENTE);
                assistente.setRevenda(revenda);
                usuarioRepository.save(assistente);
            }
        };
    }
}
