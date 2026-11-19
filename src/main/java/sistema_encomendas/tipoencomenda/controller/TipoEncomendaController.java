package sistema_encomendas.tipoencomenda.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import sistema_encomendas.tipoencomenda.dto.PrazoAlteracaoRequestDTO;
import sistema_encomendas.tipoencomenda.dto.TipoEncomendaRequestDTO;
import sistema_encomendas.tipoencomenda.dto.TipoEncomendaResponseDTO;
import sistema_encomendas.tipoencomenda.service.TipoEncomendaService;

@RestController
@RequestMapping("/tipos-encomenda")
public class TipoEncomendaController {

    private final TipoEncomendaService service;

    public TipoEncomendaController(TipoEncomendaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TipoEncomendaResponseDTO> criar(@Valid @RequestBody TipoEncomendaRequestDTO dto) {
        TipoEncomendaResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public List<TipoEncomendaResponseDTO> listar(@RequestParam(required = false) Boolean ativo,
                                                 @RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return service.buscarPorNome(nome);
        }
        return service.listar(ativo);
    }

    @GetMapping("/{id}")
    public TipoEncomendaResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TipoEncomendaResponseDTO atualizar(@PathVariable Long id,
                                              @Valid @RequestBody TipoEncomendaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @PatchMapping("/{id}/prazo")
    public TipoEncomendaResponseDTO alterarPrazo(@PathVariable Long id,
                                                 @Valid @RequestBody PrazoAlteracaoRequestDTO dto) {
        return service.alterarPrazo(id, dto);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }
}
