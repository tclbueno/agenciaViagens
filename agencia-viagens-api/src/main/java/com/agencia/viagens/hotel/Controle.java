package com.agencia.viagens.hotel;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hoteis")
public class Controle {

    private final Servico service;

    public Controle(Servico service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> buscarPorId(@PathVariable Long id) {

        Hotel hotel = service.buscarPorId(id);

        return ResponseEntity.ok(hotel);
    }

    @GetMapping
    public List<Hotel> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Hotel> criar(@Valid @RequestBody HotelRequest req) {
        Hotel hotel = service.criar(req);
        return ResponseEntity.created(URI.create("/hoteis/" + hotel.getId())).body(hotel);
    }

    @PutMapping("/{id}")
    public Hotel atualizar(@PathVariable Long id, @Valid @RequestBody HotelRequest req) {
        return service.atualizar(id, req);
    }
}
