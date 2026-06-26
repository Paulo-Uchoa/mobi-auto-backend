package com.mobiauto.backendpaulo.service;

import com.mobiauto.backendpaulo.dto.ClienteDTO;
import com.mobiauto.backendpaulo.dto.OportunidadeDTO;
import com.mobiauto.backendpaulo.dto.PerfilUsuario;
import com.mobiauto.backendpaulo.dto.Status;
import com.mobiauto.backendpaulo.dto.VeiculoDTO;
import com.mobiauto.backendpaulo.entity.Cliente;
import com.mobiauto.backendpaulo.entity.Oportunidade;
import com.mobiauto.backendpaulo.entity.Revenda;
import com.mobiauto.backendpaulo.entity.Usuario;
import com.mobiauto.backendpaulo.entity.Veiculo;
import com.mobiauto.backendpaulo.exception.AcessoNegadoException;
import com.mobiauto.backendpaulo.exception.NenhumAssistenteDisponivelException;
import com.mobiauto.backendpaulo.repository.OportunidadeRepository;
import com.mobiauto.backendpaulo.repository.RevendaRepository;
import com.mobiauto.backendpaulo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OportunidadeServiceTest {

    @Mock
    private OportunidadeRepository oportunidadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RevendaRepository revendaRepository;

    @InjectMocks
    private OportunidadeService service;

    private Revenda lojaA;

    @BeforeEach
    void setUp() {
        lojaA = revenda(1L);
    }

    @Test
    @DisplayName("Atribui a próxima oportunidade ao primeiro assistente disponível e muda o status para ATENDIMENTO")
    void atribuirProximaOportunidade_assignsFirstAssistantToFirstOpportunity() {
        Usuario assistente = usuario(2L, PerfilUsuario.ASSISTENTE, lojaA);
        Oportunidade oportunidade = oportunidade(100L, lojaA, null, Status.NOVO);

        when(usuarioRepository.findNextAvailableAssistantList(1L)).thenReturn(List.of(assistente));
        when(oportunidadeRepository.findFirstByStatusAndRevendaId(1L)).thenReturn(List.of(oportunidade));

        OportunidadeDTO result = service.atribuirProximaOportunidade(1L);

        assertThat(oportunidade.getResponsavel()).isSameAs(assistente);
        assertThat(oportunidade.getStatus()).isEqualTo(Status.ATENDIMENTO);
        assertThat(result.status()).isEqualTo(Status.ATENDIMENTO);
        verify(oportunidadeRepository).save(oportunidade);
    }

    @Test
    @DisplayName("Lança NenhumAssistenteDisponivelException quando não há assistente livre")
    void atribuirProximaOportunidade_throwsWhenNoAssistant() {
        when(usuarioRepository.findNextAvailableAssistantList(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> service.atribuirProximaOportunidade(1L))
                .isInstanceOf(NenhumAssistenteDisponivelException.class);

        verify(oportunidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lança EntityNotFoundException quando não há oportunidade disponível")
    void atribuirProximaOportunidade_throwsWhenNoOpportunity() {
        Usuario assistente = usuario(2L, PerfilUsuario.ASSISTENTE, lojaA);

        when(usuarioRepository.findNextAvailableAssistantList(1L)).thenReturn(List.of(assistente));
        when(oportunidadeRepository.findFirstByStatusAndRevendaId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> service.atribuirProximaOportunidade(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(oportunidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Gerente transfere oportunidade para outro responsável da mesma loja")
    void transferir_managerTransfersWithinSameDealership() {
        Usuario gerente = usuario(10L, PerfilUsuario.GERENTE, lojaA);
        Usuario novoResponsavel = usuario(20L, PerfilUsuario.ASSISTENTE, lojaA);
        Oportunidade oportunidade = oportunidade(5L, lojaA, usuario(30L, PerfilUsuario.ASSISTENTE, lojaA), Status.ATENDIMENTO);

        when(oportunidadeRepository.findById(5L)).thenReturn(Optional.of(oportunidade));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(novoResponsavel));

        service.transferir(5L, 20L, gerente);

        assertThat(oportunidade.getResponsavel()).isSameAs(novoResponsavel);
        verify(oportunidadeRepository).save(oportunidade);
    }

    @Test
    @DisplayName("Assistente não pode transferir oportunidade")
    void transferir_assistantIsDenied() {
        Usuario assistente = usuario(10L, PerfilUsuario.ASSISTENTE, lojaA);
        Oportunidade oportunidade = oportunidade(5L, lojaA, assistente, Status.ATENDIMENTO);

        when(oportunidadeRepository.findById(5L)).thenReturn(Optional.of(oportunidade));

        assertThatThrownBy(() -> service.transferir(5L, 20L, assistente))
                .isInstanceOf(AcessoNegadoException.class);

        verify(oportunidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Gerente não pode transferir para responsável de outra loja")
    void transferir_deniesAcrossDealerships() {
        Revenda lojaB = revenda(2L);
        Usuario gerente = usuario(10L, PerfilUsuario.GERENTE, lojaA);
        Usuario novoResponsavel = usuario(20L, PerfilUsuario.ASSISTENTE, lojaB);
        Oportunidade oportunidade = oportunidade(5L, lojaA, gerente, Status.ATENDIMENTO);

        when(oportunidadeRepository.findById(5L)).thenReturn(Optional.of(oportunidade));
        when(usuarioRepository.findById(20L)).thenReturn(Optional.of(novoResponsavel));

        assertThatThrownBy(() -> service.transferir(5L, 20L, gerente))
                .isInstanceOf(AcessoNegadoException.class);

        verify(oportunidadeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Responsável pela oportunidade pode editá-la")
    void edit_assigneeCanEdit() {
        Usuario responsavel = usuario(10L, PerfilUsuario.ASSISTENTE, lojaA);
        Oportunidade oportunidade = oportunidade(5L, lojaA, responsavel, Status.ATENDIMENTO);
        OportunidadeDTO dto = oportunidadeDto(Status.ATENDIMENTO);

        when(oportunidadeRepository.findById(5L)).thenReturn(Optional.of(oportunidade));

        OportunidadeDTO result = service.edit(5L, dto, responsavel);

        assertThat(result.status()).isEqualTo(Status.ATENDIMENTO);
        verify(oportunidadeRepository).save(oportunidade);
    }

    @Test
    @DisplayName("Usuário sem vínculo com a oportunidade não pode editá-la")
    void edit_unrelatedUserIsDenied() {
        Revenda lojaB = revenda(2L);
        Usuario responsavel = usuario(10L, PerfilUsuario.ASSISTENTE, lojaA);
        Usuario intruso = usuario(99L, PerfilUsuario.ASSISTENTE, lojaB);
        Oportunidade oportunidade = oportunidade(5L, lojaA, responsavel, Status.ATENDIMENTO);
        OportunidadeDTO dto = oportunidadeDto(Status.ATENDIMENTO);

        when(oportunidadeRepository.findById(5L)).thenReturn(Optional.of(oportunidade));

        assertThatThrownBy(() -> service.edit(5L, dto, intruso))
                .isInstanceOf(AcessoNegadoException.class);

        verify(oportunidadeRepository, never()).save(any());
    }

    private Revenda revenda(Long id) {
        Revenda revenda = new Revenda();
        revenda.setId(id);
        revenda.setCnpj("00000000000000");
        revenda.setNomeSocial("Loja " + id);
        return revenda;
    }

    private Usuario usuario(Long id, PerfilUsuario perfil, Revenda revenda) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("User " + id);
        usuario.setEmail("user" + id + "@mobiauto.com");
        usuario.setSenha("hash");
        usuario.setPerfilUsuario(perfil);
        usuario.setRevenda(revenda);
        return usuario;
    }

    private Oportunidade oportunidade(Long id, Revenda revenda, Usuario responsavel, Status status) {
        Oportunidade oportunidade = new Oportunidade();
        oportunidade.setId(id);
        oportunidade.setStatus(status);
        oportunidade.setCliente(new Cliente("Cliente", "cliente@example.com", "11999999999"));
        oportunidade.setVeiculo(new Veiculo("VW", "Gol", "1.0", "2020"));
        oportunidade.setRevenda(revenda);
        oportunidade.setResponsavel(responsavel);
        return oportunidade;
    }

    private OportunidadeDTO oportunidadeDto(Status status) {
        return new OportunidadeDTO(
                null,
                status,
                new ClienteDTO("Cliente", "cliente@example.com", "11999999999"),
                new VeiculoDTO("VW", "Gol", "1.0", "2020"),
                null,
                null,
                null,
                1L,
                null);
    }
}
