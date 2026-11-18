
package sistema_encomendas.historico.entity;

import jakarta.persistence.*;
import sistema_encomendas.encomenda.entity.Encomenda;
import sistema_encomendas.funcionario.entity.Funcionario;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "historicos_alteracao")
public class HistoricoAlteracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String campoAlterado;

    @Column(length = 500)
    private String valorAnterior;

    @Column(length = 500)
    private String valorNovo;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime dataAlteracao;

    @Column(nullable = false)
    private boolean alteracaoExcepcional = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private Encomenda encomenda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionarioResponsavel;

    // Construtor exigido pelo JPA
    protected HistoricoAlteracao() {
    }

    // Construtor para registrar uma alteração
    public HistoricoAlteracao(
            String campoAlterado,
            String valorAnterior,
            String valorNovo,
            String motivo,
            boolean alteracaoExcepcional,
            Encomenda encomenda,
            Funcionario funcionarioResponsavel) {

        this.campoAlterado = Objects.requireNonNull(campoAlterado).trim();

        if (this.campoAlterado.isEmpty()) {
            throw new IllegalArgumentException(
                    "O campo alterado deve ser informado."
            );
        }
        this.valorAnterior = valorAnterior;
        this.valorNovo = valorNovo;

        this.motivo = Objects.requireNonNull(motivo).trim();

        if (this.motivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "O motivo da alteração deve ser informado."
            );
        }
        this.dataAlteracao = LocalDateTime.now();
        this.alteracaoExcepcional = alteracaoExcepcional;
        this.encomenda = Objects.requireNonNull(encomenda);
        this.funcionarioResponsavel =
                Objects.requireNonNull(funcionarioResponsavel);
    }

    // Métodos de consulta
    public Long getId() {
        return id;
    }

    public String getCampoAlterado() {
        return campoAlterado;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public String getValorNovo() {
        return valorNovo;
    }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public boolean isAlteracaoExcepcional() {
        return alteracaoExcepcional;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public Funcionario getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }
}
