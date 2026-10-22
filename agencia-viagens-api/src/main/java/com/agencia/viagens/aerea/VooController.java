package com.agencia.viagens.aerea;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

/** Consulta de voos: a API da agência repassa a pergunta para a companhia aérea simulada. */
@RestController
@RequestMapping("/voos")
public class VooController {

    private final CompanhiaAereaClient client;

    public VooController(CompanhiaAereaClient client) {
        this.client = client;
    }

    /** GET /voos origem=São Paulo&destino=Rio de Janeiro&data=2026-12-20 (todos opcionais) */
    @GetMapping
    public List<VooDTO> listar(@RequestParam(required = false) String origem,
                               @RequestParam(required = false) String destino,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return client.listar(origem, destino, data);
    }

    @GetMapping("/{id}")
    public VooDTO buscar(@PathVariable Long id) {
        return client.buscar(id);
    }
}
