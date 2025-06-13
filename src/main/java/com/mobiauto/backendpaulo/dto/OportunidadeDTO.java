package com.mobiauto.backendpaulo.dto;

import com.mobiauto.backendpaulo.entity.*;

import static com.mobiauto.backendpaulo.util.Utils.convertLocalDateTimeToString;

public record OportunidadeDTO(Long id, Status status, ClienteDTO cliente, VeiculoDTO veiculo,
                              String dataCriacao, String dataAtualizacao, String motivoConclusao, Long revendaID, UsuarioDTO responsavel) {

    public OportunidadeDTO(Oportunidade oportunidade){
        this(oportunidade.getId(), oportunidade.getStatus(), new ClienteDTO(oportunidade.getCliente()),
                new VeiculoDTO(oportunidade.getVeiculo()), convertLocalDateTimeToString(oportunidade.getDataCriacao(), "dd/MM/yyyy HH:mm:ss"),
                convertLocalDateTimeToString(oportunidade.getDataAtualizacao(), "dd/MM/yyyy HH:mm:ss"), oportunidade.getMotivoConclusao(),
                oportunidade.getRevenda().getId(), checkOportunidadeWithoutResponsavel(oportunidade.getResponsavel()));
    }

    static UsuarioDTO checkOportunidadeWithoutResponsavel(Usuario usuario){

        if(usuario !=null){
            return new UsuarioDTO(usuario);
        }

        return null;
    }
}
