package sistema_encomendas.tipoencomenda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Dados de entrada para alterar somente o prazo (em dias) de alteração. */
public record PrazoAlteracaoRequestDTO(

        @NotNull(message = "O prazo de alteração é obrigatório")
        @Min(value = 0, message = "O prazo de alteração não pode ser negativo")
        Integer prazoAlteracaoDias
) {
}
