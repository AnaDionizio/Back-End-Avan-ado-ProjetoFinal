package sistema_encomendas.tipoencomenda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sistema_encomendas.tipoencomenda.entity.TipoEncomenda;

@Repository
public interface TipoEncomendaRepository extends JpaRepository<TipoEncomenda, Long> {

    List<TipoEncomenda> findByAtivo(boolean ativo);

    List<TipoEncomenda> findByNomeContainingIgnoreCase(String nome);
}
