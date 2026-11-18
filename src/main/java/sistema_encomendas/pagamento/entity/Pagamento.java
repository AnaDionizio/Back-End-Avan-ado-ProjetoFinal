package sistema_encomendas.pagamento.entity;

import jakarta.persistence.*;
import sistema_encomendas.encomenda.entity.Encomenda;
import sistema_encomendas.funcionario.entity.Funcionario;
import sistema_encomendas.shared.enums.FormaPagamento;
import sistema_encomendas.shared.enums.StatusEncomenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Column(length = 255)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private Encomenda encomenda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionarioResponsavel;

    // Construtor exigido pelo JPA
    protected Pagamento() {
    }

    // Construtor para registrar um pagamento
    public Pagamento(
            BigDecimal valor,
            FormaPagamento formaPagamento,
            String observacao,
            Encomenda encomenda,
            Funcionario funcionarioResponsavel) {

        this.encomenda = Objects.requireNonNull(encomenda);

        if (encomenda.getStatus() == StatusEncomenda.CANCELADA) {
            throw new IllegalStateException(
                    "Não é possível registrar pagamento para uma encomenda cancelada."
            );
        }

        this.funcionarioResponsavel =
                Objects.requireNonNull(funcionarioResponsavel);

        this.formaPagamento =
                Objects.requireNonNull(formaPagamento);

        this.dataPagamento = LocalDateTime.now();
        this.observacao = observacao;

        alterarValor(valor);
    }

    // Altera o valor do pagamento
    public void alterarValor(BigDecimal novoValor) {
        Objects.requireNonNull(novoValor);

        if (novoValor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor do pagamento deve ser maior que zero."
            );
        }

        this.valor = novoValor;
    }

    // Atualiza a forma de pagamento
    public void alterarFormaPagamento(FormaPagamento novaForma) {
        this.formaPagamento = Objects.requireNonNull(novaForma);
    }

    // Atualiza a observação
    public void atualizarObservacao(String novaObservacao) {
        this.observacao = novaObservacao;
    }

    // Métodos de consulta
    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public Funcionario getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }
}
