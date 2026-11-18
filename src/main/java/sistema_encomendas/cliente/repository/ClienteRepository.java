package sistema_encomendas.cliente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sistema_encomendas.cliente.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByAtivo(boolean ativo);

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    boolean existsByTelefone(String telefone);
}
