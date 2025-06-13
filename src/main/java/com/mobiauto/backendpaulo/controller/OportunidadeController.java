package com.mobiauto.backendpaulo.controller;

import com.mobiauto.backendpaulo.dto.OportunidadeDTO;
import com.mobiauto.backendpaulo.dto.Status;
import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.service.OportunidadeService;
import com.mobiauto.backendpaulo.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@Tag(name = "Oportunidades", description = "Métodos das Oportunidades")
@RequestMapping(value = "/oportunidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class OportunidadeController {

    @Autowired
    private OportunidadeService oportunidadeService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("")
    ResponseEntity<OportunidadeDTO> create (@RequestBody OportunidadeDTO oportunidadeDTO, UriComponentsBuilder uriBuilder){
        OportunidadeDTO response = this.oportunidadeService.create(oportunidadeDTO);

        URI uri = uriBuilder.path("/usuarios/{userID}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/atribuir/{revendaID}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<OportunidadeDTO> atribuirProximaOportunidade(@PathVariable Long revendaID) {
        return ResponseEntity.ok(this.oportunidadeService.atribuirProximaOportunidade(revendaID));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OportunidadeDTO> edit(@PathVariable Long id, @RequestBody OportunidadeDTO dto, Principal principal) {
        Usuario logado = usuarioService.getUsuarioLogado(principal.getName());
        return ResponseEntity.ok(oportunidadeService.edit(id, dto, logado));
    }

    @PutMapping("/{id}/transferir/{novoResponsavelID}")
    @PreAuthorize("hasAnyRole('GERENTE', 'PROPRIETARIO')")
    public ResponseEntity<OportunidadeDTO> transferir(
            @PathVariable Long id,
            @PathVariable Long novoResponsavelID,
            Principal principal) {

        Usuario logado = usuarioService.getUsuarioLogado(principal.getName());
        return ResponseEntity.ok(oportunidadeService.transferir(id, novoResponsavelID, logado));
    }

    @GetMapping("/{oportunidadeID}")
    ResponseEntity<OportunidadeDTO> getByID(@PathVariable Long oportunidadeID){
        return ResponseEntity.ok(this.oportunidadeService.getByID(oportunidadeID));
    }

    @GetMapping("")
    ResponseEntity<List<OportunidadeDTO>> list(){
        return ResponseEntity.ok(this.oportunidadeService.list());
    }

    @GetMapping("/status/{status}")
    ResponseEntity<List<OportunidadeDTO>> getByStatus(@PathVariable Status status){
        return ResponseEntity.ok(this.oportunidadeService.getByStatus(status));
    }

    @GetMapping("/responsavel/{userID}")
    ResponseEntity<List<OportunidadeDTO>> getByResponsavelId(@PathVariable Long userID){
        return ResponseEntity.ok(this.oportunidadeService.getByResponsavelId(userID));
    }
}
