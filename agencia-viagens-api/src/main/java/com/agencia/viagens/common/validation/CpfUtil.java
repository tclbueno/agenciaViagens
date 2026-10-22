package com.agencia.viagens.common.validation;

/** Validação de CPF pelos dígitos verificadores. */
public final class CpfUtil {

    private CpfUtil() {
    }

    public static String normalizar(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }

    public static boolean valido(String cpf) {
        String c = normalizar(cpf);
        if (c == null || c.length() != 11 || c.chars().distinct().count() == 1) {
            return false;
        }
        return digito(c, 9) == c.charAt(9) - '0' && digito(c, 10) == c.charAt(10) - '0';
    }

    private static int digito(String c, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (c.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
