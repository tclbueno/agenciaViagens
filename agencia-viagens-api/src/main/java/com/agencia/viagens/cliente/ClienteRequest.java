package com.agencia.viagens.cliente;

import com.agencia.viagens.common.validation.Cpf;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotBlank(message = "cpf é obrigatório") @Cpf String cpf,
        @NotBlank(message = "email é obrigatório") @Email(message = "email inválido") String email,
        @NotBlank(message = "senha é obrigatória") @Size(min = 6, message = "senha deve ter no mínimo 6 caracteres") String senha,
        Perfil perfil // opcional: se omitido, CLIENTE
) {
}
