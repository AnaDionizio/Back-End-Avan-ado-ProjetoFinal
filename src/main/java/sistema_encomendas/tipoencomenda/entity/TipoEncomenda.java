package sistema_encomendas.tipoencomenda.entity;

import jakarta.persistence.*;
import java.util.Objects;
import sistema_encomendas.encomenda.entity.Encomenda;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipos_encomenda")
public class TipoEncomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Integer prazoAlteracaoDias;

    @Column(nullable = false)
    private boolean ativo = true;
    @OneToMany(mappedBy = "tipoEncomenda")
    private List<Encomenda> encomendas = new ArrayList<>();

    // Construtor exigido pelo JPA
    protected TipoEncomenda() {
    }

    // Construtor para cadastrar um tipo de encomenda
    public TipoEncomenda(String nome, String descricao,
                         Integer prazoAlteracaoDias) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.descricao = descricao;
        alterarPrazoAlteracao(prazoAlteracaoDias);
        this.ativo = true;
    }

    // Atualiza os dados do tipo de encomenda
    public void atualizarDados(String nome, String descricao) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.descricao = descricao;
    }

    // Altera o prazo permitido para modificações
    public void alterarPrazoAlteracao(Integer novoPrazo) {
        Objects.requireNonNull(novoPrazo);

        if (novoPrazo < 0) {
            throw new IllegalArgumentException(
                    "O prazo de alteração não pode ser negativo."
            );
        }

        this.prazoAlteracaoDias = novoPrazo;
    }

    // Desativa o tipo de encomenda
    public void desativar() {
        this.ativo = false;
    }

    // Reativa o tipo de encomenda
    public void reativar() {
        this.ativo = true;
    }

    // Métodos de consulta
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getPrazoAlteracaoDias() {
        return prazoAlteracaoDias;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public List<Encomenda> getEncomendas() {
        return encomendas;
    }
}
