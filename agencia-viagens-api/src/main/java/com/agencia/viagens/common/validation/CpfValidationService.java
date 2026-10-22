package com.agencia.viagens.common.validation;

import org.springframework.stereotype.Service;

/** Serviço de validação e formatação de CPF (mesmo nome e métodos do exemplo da professora). */
@Service
public class CpfValidationService {

    public boolean isValid(String cpf) {
        return CpfUtil.valido(cpf);
    }

    /** 52998224725 -> 529.982.247-25 (só chamar com CPF válido). */
    public String formatar(String cpf) {
        String c = CpfUtil.normalizar(cpf);
        return c.substring(0, 3) + "." + c.substring(3, 6) + "." + c.substring(6, 9) + "-" + c.substring(9);
    }
}
