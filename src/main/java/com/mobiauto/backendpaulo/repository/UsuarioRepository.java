package com.mobiauto.backendpaulo.repository;

import com.mobiauto.backendpaulo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    @Query("""
    SELECT u FROM Usuario u
    WHERE u.perfilUsuario = 'ASSISTENTE'
      AND u.revenda.id = :revendaId
    ORDER BY
        (SELECT COUNT(o) FROM Oportunidade o
         WHERE o.responsavel = u AND o.status = 'ATENDIMENTO') ASC,
        (SELECT COALESCE(MAX(o.dataAtualizacao), '1900-01-01') FROM Oportunidade o
         WHERE o.responsavel = u) ASC
    """)
    List<Usuario> findNextAvailableAssistantList(@Param("revendaId") Long revendaId);

}
