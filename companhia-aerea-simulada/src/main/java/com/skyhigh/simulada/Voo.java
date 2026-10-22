package com.skyhigh.simulada;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Voo fictício (fica só em memória). */
public class Voo {

    private final Long id;
    private final String codigo;
    private final String origem;
    private final String destino;
    private final LocalDateTime partida;
    private final int assentosTotais;
    private int assentosDisponiveis;
    private final BigDecimal preco;

    public Voo(Long id, String codigo, String origem, String destino, LocalDateTime partida,
               int assentosTotais, String preco) {
        this.id = id;
        this.codigo = codigo;
        this.origem = origem;
        this.destino = destino;
        this.partida = partida;
        this.assentosTotais = assentosTotais;
        this.assentosDisponiveis = assentosTotais;
        this.preco = new BigDecimal(preco);
    }

    synchronized boolean reservar(int quantidade) {
        if (quantidade > assentosDisponiveis) return false;
        assentosDisponiveis -= quantidade;
        return true;
    }

    synchronized void liberar(int quantidade) {
        assentosDisponiveis = Math.min(assentosTotais, assentosDisponiveis + quantidade);
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getOrigem() { return origem; }
    public String getDestino() { return destino; }
    public LocalDateTime getPartida() { return partida; }
    public int getAssentosTotais() { return assentosTotais; }
    public synchronized int getAssentosDisponiveis() { return assentosDisponiveis; }
    public BigDecimal getPreco() { return preco; }
}
