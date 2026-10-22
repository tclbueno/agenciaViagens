package com.agencia.viagens.cliente;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest req) {
        ClienteResponse resp = ClienteResponse.de(service.criar(req));
        return ResponseEntity.created(URI.create("/clientes/" + resp.id())).body(resp);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return service.listar().stream().map(ClienteResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id) {
        return ClienteResponse.de(service.buscar(id));
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteAtualizacaoRequest req) {
        return ClienteResponse.de(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/bloquear")
    public ClienteResponse bloquear(@PathVariable Long id) {
        return ClienteResponse.de(service.definirBloqueio(id, true));
    }

    @PatchMapping("/{id}/desbloquear")
    public ClienteResponse desbloquear(@PathVariable Long id) {
        return ClienteResponse.de(service.definirBloqueio(id, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
