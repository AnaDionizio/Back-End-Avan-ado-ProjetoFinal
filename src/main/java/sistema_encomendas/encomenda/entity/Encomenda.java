package sistema_encomendas.encomenda.entity;

import jakarta.persistence.*;
import sistema_encomendas.cliente.entity.Cliente;
import sistema_encomendas.funcionario.entity.Funcionario;
import sistema_encomendas.tipoencomenda.entity.TipoEncomenda;
import sistema_encomendas.shared.enums.CanalAtendimento;
import sistema_encomendas.shared.enums.StatusEncomenda;
import sistema_encomendas.pagamento.entity.Pagamento;
import sistema_encomendas.historico.entity.HistoricoAlteracao;
import sistema_encomendas.produto.entity.Produto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Entity
@Table(name = "encomendas")
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Column(nullable = false)
    private LocalDate dataEntrega;

    @Column(length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CanalAtendimento canalAtendimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusEncomenda status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionarioResponsavel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_encomenda_id", nullable = false)
    private TipoEncomenda tipoEncomenda;

    @Column(nullable = false)
    private boolean alteracaoExcepcionalAutorizada = false;

    @OneToMany(mappedBy = "encomenda")
    private List<ItemEncomenda> itens = new ArrayList<>();

    @OneToMany(mappedBy = "encomenda")
    private List<Pagamento> pagamentos = new ArrayList<>();

    @OneToMany(mappedBy = "encomenda")
    private List<HistoricoAlteracao> historicoAlteracoes = new ArrayList<>();

    // Construtor exigido pelo JPA
    protected Encomenda() {
    }

    // Construtor para registrar uma encomenda
    public Encomenda(
            LocalDate dataEntrega,
            String observacoes,
            CanalAtendimento canalAtendimento,
            Cliente cliente,
            Funcionario funcionarioResponsavel,
            TipoEncomenda tipoEncomenda) {

        Objects.requireNonNull(dataEntrega);

        if (dataEntrega.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "A data de entrega não pode ser anterior à data atual."
            );
        }

        this.dataRegistro = LocalDateTime.now();
        this.dataEntrega = dataEntrega;
        this.observacoes = observacoes;
        this.canalAtendimento = Objects.requireNonNull(canalAtendimento);
        this.cliente = Objects.requireNonNull(cliente);
        this.funcionarioResponsavel = Objects.requireNonNull(funcionarioResponsavel);
        this.tipoEncomenda = Objects.requireNonNull(tipoEncomenda);
        this.status = StatusEncomenda.REGISTRADA;
    }

    // Verifica se a encomenda ainda pode ser alterada
    public boolean podeSerAlterada(LocalDate dataAtual) {

        Objects.requireNonNull(dataAtual);

        LocalDate limiteAlteracao = dataEntrega.minusDays(
                tipoEncomenda.getPrazoAlteracaoDias()
        );

        return !dataAtual.isAfter(limiteAlteracao);
    }

    // Autoriza uma alteração excepcional
    public void autorizarAlteracaoExcepcional() {
        this.alteracaoExcepcionalAutorizada = true;
    }

    // Atualiza informações da encomenda
    public void atualizarInformacoes(
            String novasObservacoes,
            LocalDate novaDataEntrega,
            LocalDate dataAtual) {

        Objects.requireNonNull(novaDataEntrega);
        Objects.requireNonNull(dataAtual);

        if (status == StatusEncomenda.ENTREGUE
                || status == StatusEncomenda.CANCELADA) {

            throw new IllegalStateException(
                    "Não é possível alterar uma encomenda entregue ou cancelada."
            );
        }

        if (novaDataEntrega.isBefore(dataAtual)) {
            throw new IllegalArgumentException(
                    "A nova data de entrega não pode ser anterior à data atual."
            );
        }

        if (!podeSerAlterada(dataAtual)
                && !alteracaoExcepcionalAutorizada) {

            throw new IllegalStateException(
                    "O prazo para alteração desta encomenda terminou."
            );
        }

        this.observacoes = novasObservacoes;
        this.dataEntrega = novaDataEntrega;

        // A autorização excepcional é utilizada uma única vez.
        this.alteracaoExcepcionalAutorizada = false;
    }
    // Adiciona um produto à encomenda
    public ItemEncomenda adicionarItem(
            Produto produto,
            Integer quantidade) {

        Objects.requireNonNull(produto);

        if (!produto.isAtivo()) {
            throw new IllegalStateException(
                    "Não é possível adicionar um produto inativo."
            );
        }

        ItemEncomenda item = new ItemEncomenda(
                this,
                produto,
                quantidade
        );

        itens.add(item);

        return item;
    }

    // Altera o status da encomenda
    public void alterarStatus(StatusEncomenda novoStatus) {

        Objects.requireNonNull(novoStatus);

        if (status == StatusEncomenda.ENTREGUE
                || status == StatusEncomenda.CANCELADA) {

            throw new IllegalStateException(
                    "Esta encomenda não permite novas alterações de status."
            );
        }

        if (novoStatus == StatusEncomenda.CANCELADA) {
            this.status = novoStatus;
            return;
        }

        boolean transicaoPermitida = switch (status) {

            case REGISTRADA ->
                    novoStatus == StatusEncomenda.AGUARDANDO_PRODUCAO;

            case AGUARDANDO_PRODUCAO ->
                    novoStatus == StatusEncomenda.EM_PRODUCAO;

            case EM_PRODUCAO ->
                    novoStatus == StatusEncomenda.EM_FINALIZACAO;

            case EM_FINALIZACAO ->
                    novoStatus == StatusEncomenda.PRONTA;

            case PRONTA ->
                    novoStatus == StatusEncomenda.ENTREGUE;

            default -> false;
        };

        if (!transicaoPermitida) {
            throw new IllegalStateException(
                    "Transição de status não permitida."
            );
        }

        this.status = novoStatus;
    }

    // Cancela a encomenda
    public void cancelar() {

        if (this.status == StatusEncomenda.ENTREGUE) {
            throw new IllegalStateException(
                    "Uma encomenda entregue não pode ser cancelada."
            );
        }

        if (this.status == StatusEncomenda.CANCELADA) {
            throw new IllegalStateException(
                    "Esta encomenda já está cancelada."
            );
        }

        this.status = StatusEncomenda.CANCELADA;
    }

    // Métodos de consulta
    public Long getId() {
        return id;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public CanalAtendimento getCanalAtendimento() {
        return canalAtendimento;
    }

    public StatusEncomenda getStatus() {
        return status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcionario getFuncionarioResponsavel() {
        return funcionarioResponsavel;
    }

    public TipoEncomenda getTipoEncomenda() {
        return tipoEncomenda;
    }

    public List<ItemEncomenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public List<Pagamento> getPagamentos() {
        return Collections.unmodifiableList(pagamentos);
    }

    public List<HistoricoAlteracao> getHistoricoAlteracoes() {
        return Collections.unmodifiableList(historicoAlteracoes);
    }
}