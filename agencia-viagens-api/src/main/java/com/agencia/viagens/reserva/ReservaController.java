package com.agencia.viagens.reserva;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest req) {
        ReservaResponse resp = ReservaResponse.de(service.criar(req));
        return ResponseEntity.created(URI.create("/reservas/" + resp.id())).body(resp);
    }

    @GetMapping
    public List<ReservaResponse> listar() {
        return service.listar().stream().map(ReservaResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ReservaResponse buscar(@PathVariable Long id) {
        return ReservaResponse.de(service.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public List<ReservaResponse> porCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId).stream().map(ReservaResponse::de).toList();
    }

    @PatchMapping("/{id}/confirmar")
    public ReservaResponse confirmar(@PathVariable Long id) {
        return ReservaResponse.de(service.confirmar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ReservaResponse cancelar(@PathVariable Long id) {
        return ReservaResponse.de(service.cancelar(id));
    }
}
