package sistema_encomendas.tipoencomenda.dto;

/** Dados de saída de um tipo de encomenda (a entidade nunca é exposta pela API). */
public record TipoEncomendaResponseDTO(
        Long id,
        String nome,
        String descricao,
        int prazoAlteracaoDias,
        boolean ativo
) {
}
