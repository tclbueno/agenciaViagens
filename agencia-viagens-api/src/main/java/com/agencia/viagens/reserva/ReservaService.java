package com.agencia.viagens.reserva;

import com.agencia.viagens.aerea.CompanhiaAereaClient;
import com.agencia.viagens.aerea.VooDTO;
import com.agencia.viagens.cliente.Cliente;
import com.agencia.viagens.cliente.ClienteService;
import com.agencia.viagens.common.exception.RecursoNaoEncontradoException;
import com.agencia.viagens.common.exception.RegraDeNegocioException;
import com.agencia.viagens.hotel.Hotel;
import com.agencia.viagens.hotel.Servico;
import com.agencia.viagens.pacote.Pacote;
import com.agencia.viagens.pacote.PacoteService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaService.class);

    // RN4: cancelamento gratuito com pelo menos 48h de antecedência; depois, multa de 20%
    private static final long HORAS_SEM_MULTA = 48;
    private static final BigDecimal PERCENTUAL_MULTA = new BigDecimal("0.20");

    private final ReservaRepository repository;
    private final ClienteService clienteService;
    private final PacoteService pacoteService;
    private final Servico hotelService;
    private final CompanhiaAereaClient companhiaAerea;

    public ReservaService(ReservaRepository repository, ClienteService clienteService, PacoteService pacoteService,
                          Servico hotelService, CompanhiaAereaClient companhiaAerea) {
        this.repository = repository;
        this.clienteService = clienteService;
        this.pacoteService = pacoteService;
        this.hotelService = hotelService;
        this.companhiaAerea = companhiaAerea;
    }

    public Reserva criar(ReservaRequest req) {
        // hotel é opcional (RN2), mas hotelId e numeroNoites andam juntos
        if (req.hotelId() != null && req.numeroNoites() == null) {
            throw new RegraDeNegocioException("numeroNoites é obrigatório quando hotelId é informado");
        }
        if (req.hotelId() == null && req.numeroNoites() != null) {
            throw new RegraDeNegocioException("numeroNoites só deve ser informado junto com hotelId");
        }

        // 1) cliente (RN1: precisa estar cadastrado; RN6: não pode estar bloqueado)
        Cliente cliente = clienteService.buscar(req.clienteId());
        if (cliente.isBloqueado()) {
            throw new RegraDeNegocioException("Cliente bloqueado para novas reservas");
        }

        // 2) pacote escolhido
        Pacote pacote = pacoteService.buscar(req.pacoteId());
        if (!pacote.isDisponivel()) {
            throw new RegraDeNegocioException("Pacote indisponível");
        }

        // 3) hotel, se houver
        Hotel hotel = req.hotelId() == null ? null : hotelService.buscarPorId(req.hotelId());

        // 4) COMUNICAÇÃO ENTRE APIs: consulta o voo na companhia aérea (outro projeto)
        VooDTO voo = companhiaAerea.buscar(req.vooId());
        if (!voo.partida().isAfter(LocalDateTime.now())) {
            throw new RegraDeNegocioException("O voo " + voo.codigo() + " já partiu");
        }
        if (!voo.destino().equalsIgnoreCase(pacote.getDestino())) {
            throw new RegraDeNegocioException("O voo " + voo.codigo() + " (destino " + voo.destino()
                    + ") não corresponde ao destino do pacote (" + pacote.getDestino() + ")");
        }
        if (voo.assentosDisponiveis() < req.numeroPassageiros()) {
            throw new RegraDeNegocioException("Voo sem assentos suficientes (disponíveis: " + voo.assentosDisponiveis() + ")");
        }

        // 5) valor total = pacote × passageiros (+ diárias do hotel × noites)
        BigDecimal valorTotal = pacote.getPreco().multiply(BigDecimal.valueOf(req.numeroPassageiros()));
        if (hotel != null) {
            valorTotal = valorTotal.add(hotel.getPrecoDiaria().multiply(BigDecimal.valueOf(req.numeroNoites())));
        }

        // 6) reserva os assentos na companhia aérea e registra a reserva (nasce PENDENTE, RN3)
        companhiaAerea.reservarAssentos(voo.id(), req.numeroPassageiros());

        Reserva r = new Reserva();
        r.setCliente(cliente);
        r.setPacote(pacote);
        r.setHotel(hotel);
        r.setNumeroNoites(req.numeroNoites());
        r.setVooId(voo.id());
        r.setCodigoVoo(voo.codigo());
        r.setDataHoraPartida(voo.partida());
        r.setNumeroPassageiros(req.numeroPassageiros());
        r.setValorTotal(valorTotal);
        r.setMulta(BigDecimal.ZERO.setScale(2));
        r.setStatus(StatusReserva.PENDENTE);
        r.setCriadaEm(LocalDateTime.now());
        Reserva salva = repository.save(r);

        log.info("AUDITORIA reserva criada: id={} clienteId={} pacoteId={} hotelId={} vooId={} passageiros={} valorTotal={}",
                salva.getId(), cliente.getId(), pacote.getId(), req.hotelId(), voo.id(),
                req.numeroPassageiros(), valorTotal);
        return salva;
    }

    public List<Reserva> listar() {
        return repository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva " + id + " não encontrada"));
    }

    public List<Reserva> listarPorCliente(Long clienteId) {
        clienteService.buscar(clienteId); // 404 se o cliente não existir
        return repository.findByClienteId(clienteId);
    }

    /** Registra o pagamento (RN3). Pagamento real (cartão/boleto) fica para etapas futuras. */
    public Reserva confirmar(Long id) {
        Reserva r = buscarPorId(id);
        if (r.getStatus() != StatusReserva.PENDENTE) {
            throw new RegraDeNegocioException("Só é possível confirmar reservas pendentes (status atual: " + r.getStatus() + ")");
        }
        r.setStatus(StatusReserva.CONFIRMADA);
        Reserva salva = repository.save(r);
        log.info("AUDITORIA pagamento confirmado: reservaId={}", id);
        return salva;
    }

    public Reserva cancelar(Long id) {
        Reserva r = buscarPorId(id);
        if (r.getStatus() == StatusReserva.CANCELADA) {
            throw new RegraDeNegocioException("Reserva já está cancelada");
        }

        LocalDateTime agora = LocalDateTime.now();
        if (!r.getDataHoraPartida().isAfter(agora)) {
            throw new RegraDeNegocioException("Não é possível cancelar uma viagem cujo voo já partiu");
        }

        long horasAteVoo = Duration.between(agora, r.getDataHoraPartida()).toHours();
        BigDecimal multa = horasAteVoo >= HORAS_SEM_MULTA
                ? BigDecimal.ZERO.setScale(2)
                : r.getValorTotal().multiply(PERCENTUAL_MULTA).setScale(2, RoundingMode.HALF_UP);

        // RN4: comunica o cancelamento à companhia aérea para liberar os assentos
        companhiaAerea.liberarAssentos(r.getVooId(), r.getNumeroPassageiros());

        r.setMulta(multa);
        r.setStatus(StatusReserva.CANCELADA);
        Reserva salva = repository.save(r);
        log.info("AUDITORIA reserva cancelada: id={} horasAteVoo={} multa={}", id, horasAteVoo, multa);
        return salva;
    }
}
