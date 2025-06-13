package com.mobiauto.backendpaulo.service;


import com.mobiauto.backendpaulo.dto.OportunidadeDTO;
import com.mobiauto.backendpaulo.dto.RevendaDTO;
import com.mobiauto.backendpaulo.dto.Status;
import com.mobiauto.backendpaulo.entity.Oportunidade;
import com.mobiauto.backendpaulo.entity.Revenda;
import com.mobiauto.backendpaulo.repository.OportunidadeRepository;
import com.mobiauto.backendpaulo.repository.RevendaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevendaService {

    @Autowired
    private RevendaRepository revendaRepository;

    public RevendaDTO create(RevendaDTO revenda){
        Revenda toSave = new Revenda(revenda);

        return new RevendaDTO(this.revendaRepository.save(toSave));
    }

    public List<RevendaDTO> list(){
        return this.revendaRepository.findAll().stream().map(RevendaDTO::new).toList();
    }

}
