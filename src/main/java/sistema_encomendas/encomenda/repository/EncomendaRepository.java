package sistema_encomendas.encomenda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sistema_encomendas.encomenda.entity.Encomenda;

public interface EncomendaRepository extends JpaRepository<Encomenda, Long> {
}