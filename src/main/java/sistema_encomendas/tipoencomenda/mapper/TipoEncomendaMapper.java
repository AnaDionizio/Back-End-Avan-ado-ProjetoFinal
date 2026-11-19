package sistema_encomendas.tipoencomenda.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import sistema_encomendas.tipoencomenda.dto.TipoEncomendaRequestDTO;
import sistema_encomendas.tipoencomenda.dto.TipoEncomendaResponseDTO;
import sistema_encomendas.tipoencomenda.entity.TipoEncomenda;

@Component
public class TipoEncomendaMapper {

    public TipoEncomenda toEntity(TipoEncomendaRequestDTO dto) {
        return new TipoEncomenda(dto.nome(), dto.descricao(), dto.prazoAlteracaoDias());
    }

    public TipoEncomendaResponseDTO toResponse(TipoEncomenda tipo) {
        return new TipoEncomendaResponseDTO(
                tipo.getId(),
                tipo.getNome(),
                tipo.getDescricao(),
                tipo.getPrazoAlteracaoDias(),
                tipo.isAtivo());
    }

    public List<TipoEncomendaResponseDTO> toResponseList(List<TipoEncomenda> tipos) {
        return tipos.stream().map(this::toResponse).toList();
    }
}
