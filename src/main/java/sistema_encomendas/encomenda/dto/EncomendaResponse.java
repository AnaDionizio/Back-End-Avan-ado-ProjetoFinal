package sistema_encomendas.encomenda.dto;

import sistema_encomendas.shared.enums.CanalAtendimento;
import sistema_encomendas.shared.enums.StatusEncomenda;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EncomendaResponseDTO(
        Long id,
        LocalDateTime dataRegistro,
        LocalDate dataEntrega,
        String observacoes,
        CanalAtendimento canalAtendimento,
        StatusEncomenda status,
        Long clienteId,
        Long funcionarioId,
        Long tipoEncomendaId
) {
}