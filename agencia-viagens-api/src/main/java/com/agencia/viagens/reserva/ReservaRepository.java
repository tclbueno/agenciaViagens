package com.agencia.viagens.reserva;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByClienteId(Long clienteId);
    boolean existsByClienteId(Long clienteId);
}
