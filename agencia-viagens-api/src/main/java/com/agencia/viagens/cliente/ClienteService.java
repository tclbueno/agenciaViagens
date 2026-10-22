package com.agencia.viagens.cliente;

import com.agencia.viagens.common.exception.ConflitoException;
import com.agencia.viagens.common.exception.RecursoNaoEncontradoException;
import com.agencia.viagens.common.exception.RegraDeNegocioException;
import com.agencia.viagens.common.validation.CpfUtil;
import com.agencia.viagens.reserva.ReservaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository repository;
    private final ReservaRepository reservaRepository;
    private final PasswordEncoder encoder;

    public ClienteService(ClienteRepository repository, ReservaRepository reservaRepository, PasswordEncoder encoder) {
        this.repository = repository;
        this.reservaRepository = reservaRepository;
        this.encoder = encoder;
    }

    public Cliente criar(ClienteRequest req) {
        String cpf = CpfUtil.normalizar(req.cpf());
        String email = req.email().trim().toLowerCase();
        if (repository.existsByCpf(cpf)) {
            throw new ConflitoException("Já existe um cliente com este CPF");
        }
        if (repository.existsByEmail(email)) {
            throw new ConflitoException("Já existe um cliente com este e-mail");
        }
        Cliente c = new Cliente();
        c.setNome(req.nome().trim());
        c.setCpf(cpf);
        c.setEmail(email);
        c.setSenhaHash(encoder.encode(req.senha()));
        c.setPerfil(req.perfil() == null ? Perfil.CLIENTE : req.perfil());
        c.setBloqueado(false);
        c.setCriadoEm(LocalDateTime.now());
        Cliente salvo = repository.save(c);
        log.info("AUDITORIA cliente cadastrado: id={} perfil={}", salvo.getId(), salvo.getPerfil());
        return salvo;
    }

    public List<Cliente> listar() {
        return repository.findAll();
    }

    public Cliente buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " não encontrado"));
    }

    public Cliente atualizar(Long id, ClienteAtualizacaoRequest req) {
        Cliente c = buscar(id);
        String email = req.email().trim().toLowerCase();
        if (!email.equals(c.getEmail()) && repository.existsByEmail(email)) {
            throw new ConflitoException("Já existe um cliente com este e-mail");
        }
        c.setNome(req.nome().trim());
        c.setEmail(email);
        return repository.save(c);
    }

    public Cliente definirBloqueio(Long id, boolean bloqueado) {
        Cliente c = buscar(id);
        c.setBloqueado(bloqueado);
        log.info("AUDITORIA cliente {}: id={}", bloqueado ? "bloqueado" : "desbloqueado", id);
        return repository.save(c);
    }

    public void remover(Long id) {
        Cliente c = buscar(id);
        if (reservaRepository.existsByClienteId(id)) {
            throw new RegraDeNegocioException("Cliente possui reservas e não pode ser removido");
        }
        repository.delete(c);
        log.info("AUDITORIA cliente removido: id={}", id);
    }
}
