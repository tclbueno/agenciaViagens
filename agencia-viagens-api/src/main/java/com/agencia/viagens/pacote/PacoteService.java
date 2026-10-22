package com.agencia.viagens.pacote;

import com.agencia.viagens.common.exception.RecursoNaoEncontradoException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PacoteService {

    private static final Logger log = LoggerFactory.getLogger(PacoteService.class);

    private final PacoteRepository repository;

    public PacoteService(PacoteRepository repository) {
        this.repository = repository;
    }

    public Pacote criar(PacoteRequest req) {
        Pacote p = new Pacote();
        preencher(p, req);
        Pacote salvo = repository.save(p);
        log.info("AUDITORIA pacote cadastrado: id={} nome={}", salvo.getId(), salvo.getNome());
        return salvo;
    }

    public List<Pacote> listar(String destino, Boolean disponivel) {
        return repository.findAll().stream()
                .filter(p -> destino == null || p.getDestino().equalsIgnoreCase(destino))
                .filter(p -> disponivel == null || p.isDisponivel() == disponivel)
                .toList();
    }

    public Pacote buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pacote " + id + " não encontrado"));
    }

    public Pacote atualizar(Long id, PacoteRequest req) {
        Pacote p = buscar(id);
        preencher(p, req);
        return repository.save(p);
    }

    private void preencher(Pacote p, PacoteRequest req) {
        p.setNome(req.nome().trim());
        p.setOrigem(req.origem().trim());
        p.setDestino(req.destino().trim());
        p.setDescricao(req.descricao());
        p.setPreco(req.preco());
        p.setDuracaoDias(req.duracaoDias());
        p.setDisponivel(req.disponivel() == null || req.disponivel());
    }
}
