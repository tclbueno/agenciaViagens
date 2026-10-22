package com.agencia.viagens.hotel;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Servico {

    private static final Logger log = LoggerFactory.getLogger(Servico.class);

    private final Repositorio repository;

    public Servico(Repositorio repository) {
        this.repository = repository;
    }

    public Hotel buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(NaoEncontradoEx::new);
    }

    public List<Hotel> listar() {
        return repository.findAll();
    }

    public Hotel criar(HotelRequest req) {
        Hotel hotel = new Hotel(req.nome().trim(), req.precoDiaria());
        Hotel salvo = repository.save(hotel);
        log.info("AUDITORIA hotel cadastrado: id={} nome={}", salvo.getId(), salvo.getNome());
        return salvo;
    }

    public Hotel atualizar(Long id, HotelRequest req) {
        Hotel hotel = buscarPorId(id);
        hotel.setNome(req.nome().trim());
        hotel.setPrecoDiaria(req.precoDiaria());
        return repository.save(hotel);
    }
}
