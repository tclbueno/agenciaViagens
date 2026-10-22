package com.agencia.viagens.aerea;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Voo como devolvido pela API da companhia aérea (simulada). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record VooDTO(Long id, String codigo, String origem, String destino, LocalDateTime partida,
                     Integer assentosDisponiveis, BigDecimal preco) {
}
