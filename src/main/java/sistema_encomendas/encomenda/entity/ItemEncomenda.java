package sistema_encomendas.encomenda.entity;

import jakarta.persistence.*;
import sistema_encomendas.produto.entity.Produto;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "itens_encomenda")
public class ItemEncomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encomenda_id", nullable = false)
    private Encomenda encomenda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    // Construtor exigido pelo JPA
    protected ItemEncomenda() {
    }

    // Construtor para adicionar um produto à encomenda
    // Acesso restrito ao próprio pacote
    ItemEncomenda(
            Encomenda encomenda,
            Produto produto,
            Integer quantidade) {

        this.encomenda = Objects.requireNonNull(encomenda);
        this.produto = Objects.requireNonNull(produto);

        validarQuantidade(quantidade);

        this.quantidade = quantidade;

        // Guarda o preço praticado no momento do pedido
        this.precoUnitario = Objects.requireNonNull(produto.getPreco());

        if (precoUnitario.signum() < 0) {
            throw new IllegalArgumentException(
                    "O preço do produto não pode ser negativo."
            );
        }

        calcularSubtotal();
    }

    // Valida a quantidade informada
    private void validarQuantidade(Integer quantidade) {

        Objects.requireNonNull(quantidade);

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser maior que zero."
            );
        }
    }

    // Calcula o subtotal do item
    private void calcularSubtotal() {

        this.subtotal = precoUnitario.multiply(
                BigDecimal.valueOf(quantidade)
        );
    }

    // Atualiza a quantidade do produto
    public void alterarQuantidade(Integer novaQuantidade) {

        validarQuantidade(novaQuantidade);

        this.quantidade = novaQuantidade;
        calcularSubtotal();
    }

    // Métodos de consulta
    public Long getId() {
        return id;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}