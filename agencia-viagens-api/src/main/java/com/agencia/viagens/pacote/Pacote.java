package com.agencia.viagens.pacote;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pacotes")
public class Pacote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String origem;
    private String destino;
    private String descricao;
    private BigDecimal preco; // preço por passageiro
    private Integer duracaoDias;
    private boolean disponivel;

    public Pacote() {
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getDuracaoDias() { return duracaoDias; }
    public void setDuracaoDias(Integer duracaoDias) { this.duracaoDias = duracaoDias; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}
