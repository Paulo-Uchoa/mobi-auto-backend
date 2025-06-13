package com.mobiauto.backendpaulo.repository;

import com.mobiauto.backendpaulo.dto.Status;
import com.mobiauto.backendpaulo.entity.Oportunidade;
import com.mobiauto.backendpaulo.entity.Revenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevendaRepository extends JpaRepository<Revenda, Long> {

}
