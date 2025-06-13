package com.mobiauto.backendpaulo.repository;

import com.mobiauto.backendpaulo.dto.Status;
import com.mobiauto.backendpaulo.entity.Oportunidade;
import com.mobiauto.backendpaulo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {

    List<Oportunidade> findByStatus(Status status);
    List<Oportunidade> findByResponsavelId(Long usuarioId);

    @Query("""
        SELECT o FROM Oportunidade o
        WHERE o.status = 'NOVO'
          AND o.responsavel IS NULL
          AND o.revenda.id = :revendaID
        ORDER BY o.dataCriacao ASC
        """)
    List<Oportunidade> findFirstByStatusAndRevendaId(Long revendaID);
}
