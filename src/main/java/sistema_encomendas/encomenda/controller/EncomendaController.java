package sistema_encomendas.encomenda.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema_encomendas.encomenda.entity.Encomenda;
import sistema_encomendas.encomenda.service.EncomendaService;

import java.util.List;

@RestController
@RequestMapping("/encomendas")
public class EncomendaController {

    private final EncomendaService service;

    public EncomendaController(EncomendaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Encomenda> cadastrar(
            @RequestBody Encomenda encomenda) {

        Encomenda novaEncomenda = service.salvar(encomenda);

        return ResponseEntity.ok(novaEncomenda);
    }

    @GetMapping
    public ResponseEntity<List<Encomenda>> listarTodas() {

        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encomenda> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.noContent().build();
    }
}