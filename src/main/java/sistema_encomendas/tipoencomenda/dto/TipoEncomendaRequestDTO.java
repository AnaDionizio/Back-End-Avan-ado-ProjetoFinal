package sistema_encomendas.tipoencomenda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados de entrada para criar ou atualizar um tipo de encomenda. */
public record TipoEncomendaRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
        String descricao,

        @NotNull(message = "O prazo de alteração é obrigatório")
        @Min(value = 0, message = "O prazo de alteração não pode ser negativo")
        Integer prazoAlteracaoDias
) {
}
