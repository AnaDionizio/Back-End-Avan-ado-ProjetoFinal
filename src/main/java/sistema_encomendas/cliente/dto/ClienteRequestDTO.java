package sistema_encomendas.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Dados de entrada para criar ou atualizar um cliente. */
public record ClienteRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
        String email
) {
}
