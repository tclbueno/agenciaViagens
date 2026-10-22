package com.skyhigh.simulada;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/** "Banco" de voos fictícios em memória. */
@Component
public class VooRepository {

    private final List<Voo> voos;

    public VooRepository() {
        LocalDate hoje = LocalDate.now();
        voos = List.of(
                // voos 1 a 10: um para o destino de cada pacote (o mesmo id do pacote e do hotel)
                new Voo(1L, "SKY1001", "São Paulo", "Rio de Janeiro", hoje.plusDays(60).atTime(8, 0), 100, "450.00"),
                new Voo(2L, "SKY2001", "São Paulo", "New York", hoje.plusDays(70).atTime(22, 30), 150, "3200.00"),
                new Voo(3L, "SKY3001", "São Paulo", "Paris", hoje.plusDays(75).atTime(21, 0), 150, "3800.00"),
                new Voo(4L, "SKY4001", "São Paulo", "Vancouver", hoje.plusDays(80).atTime(23, 0), 120, "3500.00"),
                new Voo(5L, "SKY5001", "São Paulo", "Tóquio", hoje.plusDays(90).atTime(1, 30), 150, "5200.00"),
                new Voo(6L, "SKY6001", "São Paulo", "Madrid", hoje.plusDays(65).atTime(20, 30), 150, "3300.00"),
                new Voo(7L, "SKY7001", "São Paulo", "Barcelona", hoje.plusDays(66).atTime(21, 15), 150, "3100.00"),
                new Voo(8L, "SKY8001", "São Paulo", "Chicago", hoje.plusDays(72).atTime(23, 45), 120, "3000.00"),
                new Voo(9L, "SKY9001", "São Paulo", "Fortaleza", hoje.plusDays(40).atTime(7, 45), 100, "700.00"),
                new Voo(10L, "SKY1010", "São Paulo", "Buenos Aires", hoje.plusDays(50).atTime(10, 0), 100, "900.00"),
                // voo amanhã: serve para testar a multa de 20% no cancelamento
                new Voo(11L, "SKY1002", "São Paulo", "Rio de Janeiro", hoje.plusDays(1).atTime(6, 0), 100, "520.00"),
                // só 2 assentos: serve para testar "voo sem assentos suficientes"
                new Voo(12L, "SKY1003", "São Paulo", "Rio de Janeiro", hoje.plusDays(30).atTime(19, 0), 2, "480.00"),
                // voo de volta
                new Voo(13L, "SKY1004", "Rio de Janeiro", "São Paulo", hoje.plusDays(65).atTime(18, 0), 100, "450.00")
        );
    }

    public List<Voo> buscar(String origem, String destino, LocalDate data) {
        return voos.stream()
                .filter(v -> origem == null || v.getOrigem().equalsIgnoreCase(origem))
                .filter(v -> destino == null || v.getDestino().equalsIgnoreCase(destino))
                .filter(v -> data == null || v.getPartida().toLocalDate().equals(data))
                .toList();
    }

    public Optional<Voo> porId(Long id) {
        return voos.stream().filter(v -> v.getId().equals(id)).findFirst();
    }
}
