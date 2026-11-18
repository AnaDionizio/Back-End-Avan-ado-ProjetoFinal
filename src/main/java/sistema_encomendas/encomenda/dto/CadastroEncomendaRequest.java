
package sistema_encomendas.encomenda.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistema_encomendas.encomenda.entity.Encomenda;
import sistema_encomendas.encomenda.repository.EncomendaRepository;
import sistema_encomendas.shared.enums.StatusEncomenda;

import java.util.List;

@Service
public class EncomendaService {

    private final EncomendaRepository repository;

    public EncomendaService(EncomendaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Encomenda salvar(Encomenda encomenda) {
        return repository.save(encomenda);
    }

    @Transactional(readOnly = true)
    public List<Encomenda> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Encomenda buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Encomenda não encontrada: id " + id
                        )
                );
    }

    @Transactional
    public Encomenda alterarStatus(Long id, StatusEncomenda novoStatus) {
        Encomenda encomenda = buscarPorId(id);
        encomenda.alterarStatus(novoStatus);
        return encomenda;
    }

    @Transactional
    public Encomenda cancelar(Long id) {
        Encomenda encomenda = buscarPorId(id);
        encomenda.cancelar();
        return encomenda;
    }
}
