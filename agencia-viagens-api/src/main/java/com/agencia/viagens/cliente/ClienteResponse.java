package com.agencia.viagens.cliente;

import java.time.LocalDateTime;

/** Nunca expõe a senha (nem o hash). */
public record ClienteResponse(Long id, String nome, String cpf, String email, Perfil perfil,
                              boolean bloqueado, LocalDateTime criadoEm) {
    public static ClienteResponse de(Cliente c) {
        return new ClienteResponse(c.getId(), c.getNome(), c.getCpf(), c.getEmail(),
                c.getPerfil(), c.isBloqueado(), c.getCriadoEm());
    }
}
