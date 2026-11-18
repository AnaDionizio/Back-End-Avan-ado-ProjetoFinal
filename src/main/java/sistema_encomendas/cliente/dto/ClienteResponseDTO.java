package sistema_encomendas.cliente.dto;

/** Dados de saída de um cliente (a entidade nunca é exposta pela API). */
public record ClienteResponseDTO(
        Long id,
        String nome,
        String telefone,
        String email,
        boolean ativo
) {
}
