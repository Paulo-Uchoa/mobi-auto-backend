package com.mobiauto.backendpaulo.service;


import com.mobiauto.backendpaulo.dto.*;
import com.mobiauto.backendpaulo.entity.*;
import com.mobiauto.backendpaulo.exception.AcessoNegadoException;
import com.mobiauto.backendpaulo.exception.NenhumAssistenteDisponivelException;
import com.mobiauto.backendpaulo.exception.UsuarioNaoEncontradoException;
import com.mobiauto.backendpaulo.repository.OportunidadeRepository;
import com.mobiauto.backendpaulo.repository.RevendaRepository;
import com.mobiauto.backendpaulo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OportunidadeService {

    @Autowired
    private OportunidadeRepository oportunidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RevendaRepository revendaRepository;

    public OportunidadeDTO create(OportunidadeDTO oportunidade){

        Revenda revenda = this.revendaRepository.getReferenceById(oportunidade.revendaID());

        Oportunidade toSave = new Oportunidade(oportunidade);
        toSave.setRevenda(revenda);

        if(oportunidade.responsavel() != null){

            Usuario responsavel = usuarioRepository.findByEmail(oportunidade.responsavel().email())
                    .orElseThrow(() -> new UsuarioNaoEncontradoException("Responsável/Usuário não encontrado"));
            toSave.setResponsavel(responsavel);
            toSave.setStatus(Status.ATENDIMENTO);
        }
        return new OportunidadeDTO(this.oportunidadeRepository.save(toSave));
    }

    public List<OportunidadeDTO> list(){
        return this.oportunidadeRepository.findAll().stream().map(OportunidadeDTO::new).toList();
    }

    public List<OportunidadeDTO> getByStatus(Status status) {
        return oportunidadeRepository.findByStatus(status).stream().map(OportunidadeDTO::new).toList();
    }

    public List<OportunidadeDTO> getByResponsavelId(Long userID) {
        return oportunidadeRepository.findByResponsavelId(userID).stream().map(OportunidadeDTO::new).toList();
    }

    public OportunidadeDTO getByID(Long userID){
        Oportunidade usuario = this.oportunidadeRepository.findById(userID).orElseThrow(() ->
                new EntityNotFoundException("Não existe Oportunidade com este ID"));

        return new OportunidadeDTO(usuario);
    }

    public OportunidadeDTO atribuirProximaOportunidade(Long revendaID) {
        List<Usuario> assistentes = usuarioRepository.findNextAvailableAssistantList(revendaID);
        if (assistentes.isEmpty()) throw new NenhumAssistenteDisponivelException("Nenhum assistente disponível");

        Usuario assistente = assistentes.get(0);

        List<Oportunidade> oportunidades = oportunidadeRepository.findFirstByStatusAndRevendaId(revendaID);
        if(oportunidades.isEmpty()) throw new EntityNotFoundException("Não existe nenhuma oportunidade disponível");

        Oportunidade oportunidade = oportunidades.get(0);

        oportunidade.setResponsavel(assistente);
        oportunidade.setStatus(Status.ATENDIMENTO);
        oportunidade.setDataAtualizacao(LocalDateTime.now());

        oportunidadeRepository.save(oportunidade);
        return new OportunidadeDTO(oportunidade);

    }

    public OportunidadeDTO edit(Long id, OportunidadeDTO dto, Usuario logado) {
        Oportunidade oportunidade = oportunidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Oportunidade não encontrada"));

        boolean isResponsavel = logado.equals(oportunidade.getResponsavel());
        boolean isGerenteOrProprietario = logado.getPerfilUsuario() == PerfilUsuario.GERENTE || logado.getPerfilUsuario() == PerfilUsuario.PROPRIETARIO;
        boolean mesmaLoja = logado.getRevenda().equals(oportunidade.getRevenda());

        if (!(isResponsavel || (isGerenteOrProprietario && mesmaLoja))) {
            throw new AcessoNegadoException("Sem permissão para editar esta oportunidade.");
        }

        oportunidade.setCliente(new Cliente(dto.cliente()));
        oportunidade.setVeiculo(new Veiculo(dto.veiculo()));
        oportunidade.setStatus(dto.status());

        if (dto.status() == Status.CONCLUIDO && dto.motivoConclusao() != null) {
            oportunidade.setMotivoConclusao(dto.motivoConclusao());
            oportunidade.setDataAtualizacao(LocalDateTime.now());
        }

        oportunidadeRepository.save(oportunidade);
        return new OportunidadeDTO(oportunidade);
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'PROPRIETARIO')")
    public OportunidadeDTO transferir(Long oportunidadeId, Long novoResponsavelID, Usuario logado) {
        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Oportunidade não encontrada"));

        if (!(logado.getPerfilUsuario() == PerfilUsuario.GERENTE || logado.getPerfilUsuario() == PerfilUsuario.PROPRIETARIO)) {
            throw new AcessoNegadoException("Apenas gerentes ou proprietários podem transferir oportunidades.");
        }

        Usuario novoResponsavel = usuarioRepository.findById(novoResponsavelID)
                .orElseThrow(() -> new EntityNotFoundException("Novo responsável não encontrado"));

        if (!logado.getRevenda().equals(novoResponsavel.getRevenda())) {
            throw new AcessoNegadoException("Você só pode transferir dentro da sua própria loja.");
        }

        oportunidade.setResponsavel(novoResponsavel);
        oportunidadeRepository.save(oportunidade);
        return new OportunidadeDTO(oportunidade);
    }

}
