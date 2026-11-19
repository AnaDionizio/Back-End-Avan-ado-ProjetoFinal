package sistema_encomendas.cliente.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import sistema_encomendas.cliente.dto.ClienteRequestDTO;
import sistema_encomendas.cliente.dto.ClienteResponseDTO;
import sistema_encomendas.cliente.entity.Cliente;

@Component
public class ClienteMapper {

    /** Converte os dados recebidos da API em uma nova entidade Cliente. */
    public Cliente toEntity(ClienteRequestDTO dto) {
        return new Cliente(
                dto.nome(),
                dto.telefone(),
                dto.email(),
                dto.endereco(),
                dto.cpf());
    }

    /** Converte a entidade Cliente nos dados devolvidos pela API. */
    public ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getEndereco(),
                cliente.getCpf(),
                cliente.isAtivo());
    }

    /** Converte uma lista de entidades em uma lista de respostas. */
    public List<ClienteResponseDTO> toResponseList(List<Cliente> clientes) {
        return clientes.stream().map(this::toResponse).toList();
    }
}
