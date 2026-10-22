package com.agencia.viagens.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteAtualizacaoRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotBlank(message = "email é obrigatório") @Email(message = "email inválido") String email
) {
}
