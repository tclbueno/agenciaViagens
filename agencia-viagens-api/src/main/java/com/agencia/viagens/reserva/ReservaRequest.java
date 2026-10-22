package com.agencia.viagens.reserva;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReservaRequest(
        @NotNull(message = "clienteId é obrigatório") Long clienteId,
        @NotNull(message = "pacoteId é obrigatório") Long pacoteId,
        @NotNull(message = "vooId é obrigatório") Long vooId,
        @NotNull(message = "numeroPassageiros é obrigatório") @Min(value = 1, message = "mínimo de 1 passageiro") Integer numeroPassageiros,
        // Opcionais (RN2): só passagem/pacote, ou com hotel
        Long hotelId,
        @Min(value = 1, message = "mínimo de 1 noite") Integer numeroNoites
) {
}
