package com.mobiauto.backendpaulo.controller;

import com.mobiauto.backendpaulo.dto.RevendaDTO;
import com.mobiauto.backendpaulo.service.RevendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/revendas", produces = MediaType.APPLICATION_JSON_VALUE)
public class RevendaController {

    @Autowired
    private RevendaService revendaService;

    @PostMapping("")
    ResponseEntity<RevendaDTO> create(@RequestBody RevendaDTO dto, UriComponentsBuilder uriBuilder) {
        RevendaDTO response = revendaService.create(dto);
        URI uri = uriBuilder.path("/revendas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("")
    ResponseEntity<List<RevendaDTO>> list() {
        return ResponseEntity.ok(revendaService.list());
    }
}
