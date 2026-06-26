package com.mobiauto.backendpaulo.service;


import com.mobiauto.backendpaulo.dto.RevendaDTO;
import com.mobiauto.backendpaulo.entity.Revenda;
import com.mobiauto.backendpaulo.repository.RevendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevendaService {

    private final RevendaRepository revendaRepository;

    public RevendaDTO create(RevendaDTO revenda){
        Revenda toSave = new Revenda(revenda);

        return new RevendaDTO(this.revendaRepository.save(toSave));
    }

    public List<RevendaDTO> list(){
        return this.revendaRepository.findAll().stream().map(RevendaDTO::new).toList();
    }

}
