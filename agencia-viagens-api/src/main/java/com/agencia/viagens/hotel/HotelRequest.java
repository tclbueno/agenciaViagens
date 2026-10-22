package com.agencia.viagens.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record HotelRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotNull(message = "precoDiaria é obrigatório") @Positive(message = "precoDiaria deve ser maior que zero") BigDecimal precoDiaria
) {
}
