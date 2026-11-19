package sistema_encomendas.funcionario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import sistema_encomendas.shared.enums.SetorFuncionario;

/**
 * Dados recebidos para cadastrar ou atualizar um funcionário.
 * A senha é obrigatória apenas no cadastro (validado no service);
 * na atualização ela é ignorada — para trocar a senha use o endpoint próprio.
 */
public record FuncionarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres")
        String email,

        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        String senha,

        @NotNull(message = "O setor é obrigatório")
        SetorFuncionario setor
) {

    /** Corpo do PATCH /funcionarios/{id}/setor */
    public record AlterarSetor(
            @NotNull(message = "O setor é obrigatório")
            SetorFuncionario setor
    ) {
    }

    /** Corpo do PATCH /funcionarios/{id}/senha */
    public record AlterarSenha(
            @NotBlank(message = "A nova senha é obrigatória")
            @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
            String novaSenha
    ) {
    }
}