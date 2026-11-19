package sistema_encomendas.funcionario.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

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

import sistema_encomendas.funcionario.dto.FuncionarioRequestDTO;
import sistema_encomendas.funcionario.dto.FuncionarioResponseDTO;
import sistema_encomendas.funcionario.service.FuncionarioService;
import sistema_encomendas.shared.enums.SetorFuncionario;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    /**
     * GET /funcionarios
     * Filtros opcionais: ?ativo=true | ?setor=XYZ | ?nome=ana
     */
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listar(
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) SetorFuncionario setor,
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(service.listar(ativo, setor, nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(
            @RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO criado = service.cadastrar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FuncionarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PatchMapping("/{id}/setor")
    public ResponseEntity<FuncionarioResponseDTO> alterarSetor(
            @PathVariable Long id,
            @RequestBody @Valid FuncionarioRequestDTO.AlterarSetor dto) {
        return ResponseEntity.ok(service.alterarSetor(id, dto.setor()));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable Long id,
            @RequestBody @Valid FuncionarioRequestDTO.AlterarSenha dto) {
        service.alterarSenha(id, dto.novaSenha());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<FuncionarioResponseDTO> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.ativar(id));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<FuncionarioResponseDTO> desativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.desativar(id));
    }
}