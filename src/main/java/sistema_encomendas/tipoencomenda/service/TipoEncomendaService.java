package sistema_encomendas.tipoencomenda.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistema_encomendas.shared.exception.RecursoNaoEncontradoException;
import sistema_encomendas.tipoencomenda.dto.PrazoAlteracaoRequestDTO;
import sistema_encomendas.tipoencomenda.dto.TipoEncomendaRequestDTO;
import sistema_encomendas.tipoencomenda.dto.TipoEncomendaResponseDTO;
import sistema_encomendas.tipoencomenda.entity.TipoEncomenda;
import sistema_encomendas.tipoencomenda.mapper.TipoEncomendaMapper;
import sistema_encomendas.tipoencomenda.repository.TipoEncomendaRepository;

@Service
public class TipoEncomendaService {

    private final TipoEncomendaRepository repository;
    private final TipoEncomendaMapper mapper;

    public TipoEncomendaService(TipoEncomendaRepository repository, TipoEncomendaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TipoEncomendaResponseDTO criar(TipoEncomendaRequestDTO dto) {
        TipoEncomenda salvo = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<TipoEncomendaResponseDTO> listar(Boolean ativo) {
        List<TipoEncomenda> tipos = (ativo == null) ? repository.findAll() : repository.findByAtivo(ativo);
        return mapper.toResponseList(tipos);
    }

    @Transactional(readOnly = true)
    public List<TipoEncomendaResponseDTO> buscarPorNome(String nome) {
        return mapper.toResponseList(repository.findByNomeContainingIgnoreCase(nome));
    }

    @Transactional(readOnly = true)
    public TipoEncomendaResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(obterEntidade(id));
    }

    @Transactional
    public TipoEncomendaResponseDTO atualizar(Long id, TipoEncomendaRequestDTO dto) {
        TipoEncomenda tipo = obterEntidade(id);
        tipo.atualizarDados(dto.nome(), dto.descricao());
        tipo.alterarPrazoAlteracao(dto.prazoAlteracaoDias());
        return mapper.toResponse(tipo);
    }

    @Transactional
    public TipoEncomendaResponseDTO alterarPrazo(Long id, PrazoAlteracaoRequestDTO dto) {
        TipoEncomenda tipo = obterEntidade(id);
        tipo.alterarPrazoAlteracao(dto.prazoAlteracaoDias());
        return mapper.toResponse(tipo);
    }

    @Transactional
    public void desativar(Long id) {
        obterEntidade(id).desativar();
    }

    @Transactional
    public void reativar(Long id) {
        obterEntidade(id).reativar();
    }

    /** Usado também por outros módulos (ex.: EncomendaService). */
    @Transactional(readOnly = true)
    public TipoEncomenda obterEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tipo de encomenda não encontrado: id " + id));
    }
}
