package com.agencia.viagens.reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        Long clienteId,
        String nomeCliente,
        Long pacoteId,
        String nomePacote,
        Long hotelId,
        String nomeHotel,
        Integer numeroNoites,
        Long vooId,
        String codigoVoo,
        LocalDateTime dataHoraPartida,
        Integer numeroPassageiros,
        BigDecimal valorTotal,
        BigDecimal multa,
        StatusReserva status,
        LocalDateTime criadaEm
) {
    public static ReservaResponse de(Reserva r) {
        return new ReservaResponse(
                r.getId(),
                r.getCliente().getId(), r.getCliente().getNome(),
                r.getPacote().getId(), r.getPacote().getNome(),
                r.getHotel() == null ? null : r.getHotel().getId(),
                r.getHotel() == null ? null : r.getHotel().getNome(),
                r.getNumeroNoites(),
                r.getVooId(), r.getCodigoVoo(), r.getDataHoraPartida(),
                r.getNumeroPassageiros(), r.getValorTotal(), r.getMulta(),
                r.getStatus(), r.getCriadaEm());
    }
}
