
package sistema_encomendas.encomenda.mapper;

import org.springframework.stereotype.Component;

import sistema_encomendas.encomenda.dto.EncomendaResponseDTO;
import sistema_encomendas.encomenda.entity.Encomenda;

import java.util.List;

@Component
public class EncomendaMapper {

    public EncomendaResponseDTO toResponse(Encomenda encomenda) {
        return new EncomendaResponseDTO(
                encomenda.getId(),
                encomenda.getDataRegistro(),
                encomenda.getDataEntrega(),
                encomenda.getObservacoes(),
                encomenda.getCanalAtendimento(),
                encomenda.getStatus(),
                encomenda.getCliente().getId(),
                encomenda.getFuncionarioResponsavel().getId(),
                encomenda.getTipoEncomenda().getId()
        );
    }

    public List<EncomendaResponseDTO> toResponseList(
            List<Encomenda> encomendas) {

        return encomendas.stream()
                .map(this::toResponse)
                .toList();
    }
}
