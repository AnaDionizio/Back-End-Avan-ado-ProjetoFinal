
package sistema_encomendas.funcionario.entity;

import jakarta.persistence.*;
import java.util.Objects;
import sistema_encomendas.shared.enums.SetorFuncionario;
import sistema_encomendas.encomenda.entity.Encomenda;
import sistema_encomendas.pagamento.entity.Pagamento;
import sistema_encomendas.historico.entity.HistoricoAlteracao;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SetorFuncionario setor;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "funcionarioResponsavel")
    private List<Encomenda> encomendasRegistradas = new ArrayList<>();

    @OneToMany(mappedBy = "funcionarioResponsavel")
    private List<Pagamento> pagamentosRegistrados = new ArrayList<>();

    @OneToMany(mappedBy = "funcionarioResponsavel")
    private List<HistoricoAlteracao> historicosAlteracao = new ArrayList<>();

    protected Funcionario() {
    }

    public Funcionario(String nome, String email,
                       String senhaHash, SetorFuncionario setor) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.email = Objects.requireNonNull(email).trim();
        this.senhaHash = Objects.requireNonNull(senhaHash);
        this.setor = Objects.requireNonNull(setor);
        this.ativo = true;
    }

    public void atualizarDados(String nome, String email,
                               SetorFuncionario setor) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.email = Objects.requireNonNull(email).trim();
        this.setor = Objects.requireNonNull(setor);
    }

    public void alterarSenha(String novaSenhaHash) {
        this.senhaHash = Objects.requireNonNull(novaSenhaHash);
    }

    public void desativar() {
        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public SetorFuncionario getSetor() {
        return setor;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public List<Encomenda> getEncomendasRegistradas() {
        return encomendasRegistradas;
    }

    public List<Pagamento> getPagamentosRegistrados() {
        return pagamentosRegistrados;
    }

    public List<HistoricoAlteracao> getHistoricosAlteracao() {
        return historicosAlteracao;
    }
}
