package sistema_encomendas.cliente.entity;

import jakarta.persistence.*;
import sistema_encomendas.encomenda.entity.Encomenda;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "clientes")
public class Cliente {

    // ===================== Atributos =====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(length = 120)
    private String email;

    @Column(length = 255)
    private String endereco;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "cliente")
    private List<Encomenda> encomendas = new ArrayList<>();

    // ===================== Construtores =====================

    /** Construtor exigido pelo JPA. */
    protected Cliente() {
    }

    /** Construtor para cadastrar um novo cliente. */
    public Cliente(String nome, String telefone, String email,
                   String endereco, String cpf) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.telefone = Objects.requireNonNull(telefone).trim();
        this.email = email;
        this.endereco = endereco;
        this.cpf = Objects.requireNonNull(cpf).trim();
        this.ativo = true;
    }

    // ===================== Métodos de alteração =====================

    public void atualizarDados(String nome, String telefone, String email,
                               String endereco, String cpf) {
        this.nome = Objects.requireNonNull(nome).trim();
        this.telefone = Objects.requireNonNull(telefone).trim();
        this.email = email;
        this.endereco = endereco;
        this.cpf = Objects.requireNonNull(cpf).trim();
    }

    public void desativar() {
        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }

    // ===================== Getters =====================

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public List<Encomenda> getEncomendas() {
        return encomendas;
    }
}