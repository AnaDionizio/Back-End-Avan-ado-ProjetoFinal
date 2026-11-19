package sistema_encomendas.funcionario.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import sistema_encomendas.funcionario.dto.FuncionarioRequestDTO;
import sistema_encomendas.funcionario.dto.FuncionarioResponseDTO;
import sistema_encomendas.funcionario.entity.Funcionario;

@Component
public class FuncionarioMapper {

    /**
     * Cria a entidade a partir do DTO. A senha já deve chegar com hash,
     * pois o mapper não é responsável por criptografia.
     */
    public Funcionario toEntity(FuncionarioRequestDTO dto, String senhaHash) {
        return new Funcionario(
                dto.nome(),
                dto.email(),
                senhaHash,
                dto.setor()
        );
    }

    public FuncionarioResponseDTO toResponse(Funcionario funcionario) {
        return new FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getSetor(),
                funcionario.isAtivo()
        );
    }

    public List<FuncionarioResponseDTO> toResponseList(List<Funcionario> funcionarios) {
        return funcionarios.stream()
                .map(this::toResponse)
                .toList();
    }
}