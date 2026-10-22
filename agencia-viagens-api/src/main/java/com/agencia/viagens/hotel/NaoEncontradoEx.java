package com.agencia.viagens.hotel;

import com.agencia.viagens.common.exception.RecursoNaoEncontradoException;

/** Exceção da API de hotéis. Herda da exceção comum para o GlobalExceptionHandler devolver 404. */
public class NaoEncontradoEx extends RecursoNaoEncontradoException {

    public NaoEncontradoEx() {
        super("Hotel não encontrado.");
    }
}
