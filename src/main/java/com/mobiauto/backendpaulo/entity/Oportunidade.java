package com.mobiauto.backendpaulo.entity;


import com.mobiauto.backendpaulo.dto.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NOVO;

    @Embedded
    private Cliente cliente;

    @Embedded
    private Veiculo veiculo;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "responsavel_id", insertable=false, updatable=false)
    private Usuario responsavel;

    @ManyToOne(optional = false)
    private Revenda revenda;

    @Column
    private String motivoConclusao;

    public Oportunidade(OportunidadeDTO oportunidade) {
        this.cliente = new Cliente(oportunidade.cliente());
        this.veiculo = new Veiculo(oportunidade.veiculo());
        this.dataCriacao = LocalDateTime.now();
    }
}
