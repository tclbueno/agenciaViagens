package com.agencia.viagens.hotel;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hoteis")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal precoDiaria;

    public Hotel() {
    }

    public Hotel(String nome, BigDecimal precoDiaria) {
        this.nome = nome;
        this.precoDiaria = precoDiaria;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoDiaria() {
        return precoDiaria;
    }

    public void setPrecoDiaria(BigDecimal precoDiaria) {
        this.precoDiaria = precoDiaria;
    }
}
