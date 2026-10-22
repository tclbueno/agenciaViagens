package com.agencia.viagens.pacote;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacotes")
public class PacoteController {

    private final PacoteService service;

    public PacoteController(PacoteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pacote> criar(@Valid @RequestBody PacoteRequest req) {
        Pacote p = service.criar(req);
        return ResponseEntity.created(URI.create("/pacotes/" + p.getId())).body(p);
    }

    /** Filtros opcionais: GET /pacotes?destino=Salvador&disponivel=true */
    @GetMapping
    public List<Pacote> listar(@RequestParam(required = false) String destino,
                               @RequestParam(required = false) Boolean disponivel) {
        return service.listar(destino, disponivel);
    }

    @GetMapping("/{id}")
    public Pacote buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public Pacote atualizar(@PathVariable Long id, @Valid @RequestBody PacoteRequest req) {
        return service.atualizar(id, req);
    }
}
