package sistema_encomendas.encomenda.service;

import org.springframework.stereotype.Service;
import sistema_encomendas.encomenda.entity.Encomenda;
import sistema_encomendas.encomenda.repository.EncomendaRepository;

import java.util.List;

@Service
public class EncomendaService {

    private final EncomendaRepository repository;

    public EncomendaService(EncomendaRepository repository) {
        this.repository = repository;
    }

    public Encomenda salvar(Encomenda encomenda) {
        return repository.save(encomenda);
    }

    public List<Encomenda> listarTodas() {
        return repository.findAll();
    }

    public Encomenda buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Encomenda não encontrada."
                        )
                );
    }

    public void excluir(Long id) {
        Encomenda encomenda = buscarPorId(id);
        repository.delete(encomenda);
    }
}