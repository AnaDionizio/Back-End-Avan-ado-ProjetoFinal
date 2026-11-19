package sistema_encomendas.funcionario.dto;

import sistema_encomendas.shared.enums.SetorFuncionario;

/**
 * Dados devolvidos pela API. Nunca expõe a senha.
 */
public record FuncionarioResponseDTO(
        Long id,
        String nome,
        String email,
        SetorFuncionario setor,
        boolean ativo
) {
}