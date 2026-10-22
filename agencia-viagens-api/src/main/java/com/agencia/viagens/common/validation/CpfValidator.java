package com.agencia.viagens.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {
    @Override
    public boolean isValid(String valor, ConstraintValidatorContext ctx) {
        return valor == null || CpfUtil.valido(valor); // @NotBlank cuida do vazio
    }
}
