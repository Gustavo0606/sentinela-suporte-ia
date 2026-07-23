package br.com.sentinela.core_api.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_triagens_ia")
@Getter
@Setter
public class TriagemIA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sentimento sentimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private int urgencia;

    @Column(nullable = false)
    private String resumo;

    @OneToOne
    @JoinColumn(name = "chamado_id", nullable = false)
    private Chamado chamado;
}
