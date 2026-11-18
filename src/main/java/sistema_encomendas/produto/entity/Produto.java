
package sistema_encomendas.produto.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import sistema_encomendas.encomenda.entity.ItemEncomenda;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "produto")
    private List<ItemEncomenda> itensEncomenda = new ArrayList<>();

    // Construtor exigido pelo JPA
    protected Produto() {
    }

    // Construtor para cadastrar um produto
    public Produto(String nome, String descricao, BigDecimal preco) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.descricao = descricao;
        alterarPreco(preco);
        this.ativo = true;
    }

    // Atualiza os dados do produto
    public void atualizarDados(String nome, String descricao) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.descricao = descricao;
    }

    // Altera o preço do produto
    public void alterarPreco(BigDecimal novoPreco) {
        Objects.requireNonNull(novoPreco);

        if (novoPreco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "O preço não pode ser negativo."
            );
        }

        this.preco = novoPreco;
    }

    // Desativa o produto
    public void desativar() {
        this.ativo = false;
    }

    // Reativa o produto
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

    public BigDecimal getPreco() {
        return preco;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public List<ItemEncomenda> getItensEncomenda() {
        return itensEncomenda;
    }
}
