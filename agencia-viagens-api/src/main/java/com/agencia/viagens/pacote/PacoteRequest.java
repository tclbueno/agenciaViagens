package com.agencia.viagens.pacote;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PacoteRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotBlank(message = "origem é obrigatória") String origem,
        @NotBlank(message = "destino é obrigatório") String destino,
        String descricao,
        @NotNull(message = "preco é obrigatório") @Positive(message = "preco deve ser maior que zero") BigDecimal preco,
        @NotNull(message = "duracaoDias é obrigatório") @Min(value = 1, message = "duracaoDias mínimo é 1") Integer duracaoDias,
        Boolean disponivel // opcional: se omitido, true
) {
}
