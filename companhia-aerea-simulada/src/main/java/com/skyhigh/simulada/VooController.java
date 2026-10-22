package com.skyhigh.simulada;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/** API simulada da companhia aérea (SkyHigh): consulta voos e reserva/libera assentos. */
@RestController
@RequestMapping("/voos")
public class VooController {

    private final VooRepository repository;

    public VooController(VooRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Voo> buscar(@RequestParam(required = false) String origem,
                            @RequestParam(required = false) String destino,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return repository.buscar(origem, destino, data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> porId(@PathVariable Long id) {
        return repository.porId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> naoEncontrado(id));
    }

    @PostMapping("/{id}/reservar-assentos")
    public ResponseEntity<?> reservar(@PathVariable Long id, @Valid @RequestBody AssentosRequest req) {
        var voo = repository.porId(id);
        if (voo.isEmpty()) return naoEncontrado(id);
        if (!voo.get().reservar(req.quantidade())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(java.util.Map.of("erro", "Assentos insuficientes no voo " + voo.get().getCodigo()));
        }
        return ResponseEntity.ok(voo.get());
    }

    @PostMapping("/{id}/liberar-assentos")
    public ResponseEntity<?> liberar(@PathVariable Long id, @Valid @RequestBody AssentosRequest req) {
        var voo = repository.porId(id);
        if (voo.isEmpty()) return naoEncontrado(id);
        voo.get().liberar(req.quantidade());
        return ResponseEntity.ok(voo.get());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> invalido(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(java.util.Map.of("erro", "quantidade inválida (mínimo 1)"));
    }

    private ResponseEntity<?> naoEncontrado(Long id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("erro", "Voo " + id + " não encontrado"));
    }
}
