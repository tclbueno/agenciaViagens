package com.agencia.viagens.reserva;

import com.agencia.viagens.cliente.Cliente;
import com.agencia.viagens.hotel.Hotel;
import com.agencia.viagens.pacote.Pacote;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private Pacote pacote;

    @ManyToOne // opcional (RN2)
    private Hotel hotel;

    private Integer numeroNoites;

    // Dados do voo na companhia aérea
    private Long vooId;
    private String codigoVoo;
    private LocalDateTime dataHoraPartida;

    private Integer numeroPassageiros;
    private BigDecimal valorTotal;
    private BigDecimal multa;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    private LocalDateTime criadaEm;

    public Reserva() {
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Pacote getPacote() { return pacote; }
    public void setPacote(Pacote pacote) { this.pacote = pacote; }
    public Hotel getHotel() { return hotel; }
    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public Integer getNumeroNoites() { return numeroNoites; }
    public void setNumeroNoites(Integer numeroNoites) { this.numeroNoites = numeroNoites; }
    public Long getVooId() { return vooId; }
    public void setVooId(Long vooId) { this.vooId = vooId; }
    public String getCodigoVoo() { return codigoVoo; }
    public void setCodigoVoo(String codigoVoo) { this.codigoVoo = codigoVoo; }
    public LocalDateTime getDataHoraPartida() { return dataHoraPartida; }
    public void setDataHoraPartida(LocalDateTime dataHoraPartida) { this.dataHoraPartida = dataHoraPartida; }
    public Integer getNumeroPassageiros() { return numeroPassageiros; }
    public void setNumeroPassageiros(Integer numeroPassageiros) { this.numeroPassageiros = numeroPassageiros; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public BigDecimal getMulta() { return multa; }
    public void setMulta(BigDecimal multa) { this.multa = multa; }
    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public void setCriadaEm(LocalDateTime criadaEm) { this.criadaEm = criadaEm; }
}
