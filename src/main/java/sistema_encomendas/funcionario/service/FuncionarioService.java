package sistema_encomendas.funcionario.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sistema_encomendas.funcionario.dto.FuncionarioRequestDTO;
import sistema_encomendas.funcionario.dto.FuncionarioResponseDTO;
import sistema_encomendas.funcionario.entity.Funcionario;
import sistema_encomendas.funcionario.mapper.FuncionarioMapper;
import sistema_encomendas.funcionario.repository.FuncionarioRepository;
import sistema_encomendas.shared.enums.SetorFuncionario;
import sistema_encomendas.shared.exception.RecursoNaoEncontradoException;
import sistema_encomendas.shared.exception.RegraDeNegocioException;

@Service
public class FuncionarioService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final FuncionarioRepository repository;
    private final FuncionarioMapper mapper;

    public FuncionarioService(FuncionarioRepository repository, FuncionarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // ---------- Consultas ----------

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listar(Boolean ativo, SetorFuncionario setor, String nome) {
        List<Funcionario> funcionarios;

        if (nome != null && !nome.isBlank()) {
            funcionarios = repository.findByNomeContainingIgnoreCase(nome.trim());
        } else if (setor != null && ativo != null) {
            funcionarios = repository.findBySetorAndAtivo(setor, ativo);
        } else if (setor != null) {
            funcionarios = repository.findBySetor(setor);
        } else if (ativo != null) {
            funcionarios = repository.findByAtivo(ativo);
        } else {
            funcionarios = repository.findAll();
        }

        return mapper.toResponseList(funcionarios);
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Long id) {
        return mapper.toResponse(buscarEntidade(id));
    }

    // ---------- Cadastro e atualização ----------

    @Transactional
    public FuncionarioResponseDTO cadastrar(FuncionarioRequestDTO dto) {
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new RegraDeNegocioException("A senha é obrigatória no cadastro");
        }
        if (repository.existsByEmailIgnoreCase(dto.email().trim())) {
            throw new RegraDeNegocioException("Já existe um funcionário com este e-mail");
        }

        Funcionario funcionario = mapper.toEntity(dto, gerarHash(dto.senha()));
        return mapper.toResponse(repository.save(funcionario));
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = buscarEntidade(id);

        if (repository.existsByEmailIgnoreCaseAndIdNot(dto.email().trim(), id)) {
            throw new RegraDeNegocioException("Já existe outro funcionário com este e-mail");
        }

        funcionario.atualizarDados(dto.nome(), dto.email(), dto.setor());
        return mapper.toResponse(funcionario);
    }

    // ---------- Setor ----------

    @Transactional
    public FuncionarioResponseDTO alterarSetor(Long id, SetorFuncionario novoSetor) {
        Funcionario funcionario = buscarEntidade(id);
        funcionario.atualizarDados(funcionario.getNome(), funcionario.getEmail(), novoSetor);
        return mapper.toResponse(funcionario);
    }

    // ---------- Senha ----------

    @Transactional
    public void alterarSenha(Long id, String novaSenha) {
        Funcionario funcionario = buscarEntidade(id);
        funcionario.alterarSenha(gerarHash(novaSenha));
    }

    // ---------- Ativação ----------

    @Transactional
    public FuncionarioResponseDTO ativar(Long id) {
        Funcionario funcionario = buscarEntidade(id);
        if (funcionario.isAtivo()) {
            throw new RegraDeNegocioException("O funcionário já está ativo");
        }
        funcionario.reativar();
        return mapper.toResponse(funcionario);
    }

    @Transactional
    public FuncionarioResponseDTO desativar(Long id) {
        Funcionario funcionario = buscarEntidade(id);
        if (!funcionario.isAtivo()) {
            throw new RegraDeNegocioException("O funcionário já está inativo");
        }
        funcionario.desativar();
        return mapper.toResponse(funcionario);
    }

    // ---------- Auxiliares ----------

    private Funcionario buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Funcionário não encontrado com id " + id));
    }

    /**
     * Gera o hash da senha com SHA-256 e um salt aleatório, no formato "salt:hash".
     * Não depende de bibliotecas externas. Se o projeto adicionar o Spring Security,
     * o ideal é trocar por BCryptPasswordEncoder.
     */
    private String gerarHash(String senha) {
        try {
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(salt) + ":" + encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo de hash indisponível", e);
        }
    }
}