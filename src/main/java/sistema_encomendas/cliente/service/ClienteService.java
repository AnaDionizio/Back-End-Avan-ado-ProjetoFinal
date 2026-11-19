package sistema_encomendas.cliente.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistema_encomendas.cliente.dto.ClienteRequestDTO;
import sistema_encomendas.cliente.dto.ClienteResponseDTO;
import sistema_encomendas.cliente.entity.Cliente;
import sistema_encomendas.cliente.mapper.ClienteMapper;
import sistema_encomendas.cliente.repository.ClienteRepository;
import sistema_encomendas.shared.exception.RecursoNaoEncontradoException;
import sistema_encomendas.shared.exception.RegraDeNegocioException;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteService(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        if (repository.existsByCpf(dto.cpf())) {
            throw new RegraDeNegocioException("Já existe um cliente cadastrado com o CPF " + dto.cpf());
        }
        Cliente salvo = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar(Boolean ativo) {
        List<Cliente> clientes = (ativo == null) ? repository.findAll() : repository.findByAtivo(ativo);
        return mapper.toResponseList(clientes);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNome(String nome) {
        return mapper.toResponseList(repository.findByNomeContainingIgnoreCase(nome));
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(obterEntidade(id));
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = obterEntidade(id);

        if (repository.existsByCpfAndIdNot(dto.cpf(), id)) {
            throw new RegraDeNegocioException("Já existe outro cliente cadastrado com o CPF " + dto.cpf());
        }

        cliente.atualizarDados(
                dto.nome(),
                dto.telefone(),
                dto.email(),
                dto.endereco(),
                dto.cpf());

        return mapper.toResponse(cliente);
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
    public Cliente obterEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: id " + id));
    }
}
