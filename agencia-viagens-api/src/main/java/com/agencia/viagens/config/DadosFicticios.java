package com.agencia.viagens.config;

import com.agencia.viagens.cliente.Cliente;
import com.agencia.viagens.cliente.ClienteRepository;
import com.agencia.viagens.cliente.Perfil;
import com.agencia.viagens.hotel.Hotel;
import com.agencia.viagens.hotel.Repositorio;
import com.agencia.viagens.pacote.Pacote;
import com.agencia.viagens.pacote.PacoteRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Dados fictícios carregados ao iniciar (a senha de todos os usuários é "senha123"). */
@Configuration
public class DadosFicticios {

    @Bean
    CommandLineRunner carregar(ClienteRepository clientes, Repositorio hoteis,
                               PacoteRepository pacotes, PasswordEncoder encoder) {
        return args -> {
            if (clientes.count() > 0) return;

            String hash = encoder.encode("senha123");
            clientes.save(cliente("Dona da Agência", "52998224725", "dona@mundoviagens.com", hash, Perfil.ADMINISTRADOR));
            clientes.save(cliente("Atendente Ana", "11144477735", "ana@mundoviagens.com", hash, Perfil.FUNCIONARIO));
            clientes.save(cliente("Maria Silva", "39053344705", "maria@email.com", hash, Perfil.CLIENTE));
            clientes.save(cliente("João Souza", "12345678909", "joao@email.com", hash, Perfil.CLIENTE));

            // Hotéis cadastrados pela integrante responsável pela API de hotéis (mesmos nomes e preços)
            hoteis.save(new Hotel("Hotel Rio de Janeiro", new BigDecimal("950.00")));
            hoteis.save(new Hotel("Hotel New York", new BigDecimal("1000.00")));
            hoteis.save(new Hotel("Hotel Paris", new BigDecimal("1050.00")));
            hoteis.save(new Hotel("Hotel Vancouver", new BigDecimal("900.00")));
            hoteis.save(new Hotel("Hotel Tóquio", new BigDecimal("1100.00")));
            hoteis.save(new Hotel("Hotel Madrid", new BigDecimal("900.00")));
            hoteis.save(new Hotel("Hotel Barcelona", new BigDecimal("950.00")));
            hoteis.save(new Hotel("Hotel Chicago", new BigDecimal("800.00")));
            hoteis.save(new Hotel("Hotel Fortaleza", new BigDecimal("900.00")));
            hoteis.save(new Hotel("Hotel Buenos Aires", new BigDecimal("800.00")));

            // Um pacote para cada hotel: pacote N, hotel N e voo N têm o mesmo destino (ids 1 a 10)
            pacotes.save(pacote("Rio de Janeiro 5 dias", "Rio de Janeiro", "Praias, Pão de Açúcar e Cristo Redentor", "3200.00", 5, true));
            pacotes.save(pacote("New York 7 dias", "New York", "Times Square, Central Park e Estátua da Liberdade", "9800.00", 7, true));
            pacotes.save(pacote("Paris 8 dias", "Paris", "Torre Eiffel, Louvre e passeio pelo Sena", "11500.00", 8, true));
            pacotes.save(pacote("Vancouver 7 dias", "Vancouver", "Stanley Park, montanhas e culinária local", "9200.00", 7, true));
            pacotes.save(pacote("Tóquio 10 dias", "Tóquio", "Templos, tecnologia e gastronomia japonesa", "14500.00", 10, true));
            pacotes.save(pacote("Madrid 7 dias", "Madrid", "Museu do Prado, Palácio Real e tapas", "8600.00", 7, true));
            pacotes.save(pacote("Barcelona 6 dias", "Barcelona", "Sagrada Família, Parque Güell e praias", "8200.00", 6, true));
            pacotes.save(pacote("Chicago 6 dias", "Chicago", "Arquitetura, Millennium Park e jazz", "8900.00", 6, true));
            pacotes.save(pacote("Fortaleza 5 dias", "Fortaleza", "Praias, jangadas e passeio às dunas", "2900.00", 5, true));
            pacotes.save(pacote("Buenos Aires 4 dias", "Buenos Aires", "Tango, San Telmo e parrilla", "3400.00", 4, true));
            // sem hotel e sem voo: serve para testar "Pacote indisponível"
            pacotes.save(pacote("Gramado 3 dias", "Gramado", "Pacote de inverno (esgotado)", "1800.00", 3, false));
        };
    }

    private Cliente cliente(String nome, String cpf, String email, String hash, Perfil perfil) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setCpf(cpf);
        c.setEmail(email);
        c.setSenhaHash(hash);
        c.setPerfil(perfil);
        c.setBloqueado(false);
        c.setCriadoEm(LocalDateTime.now());
        return c;
    }

    private Pacote pacote(String nome, String destino, String descricao, String preco, int dias, boolean disponivel) {
        Pacote p = new Pacote();
        p.setNome(nome);
        p.setOrigem("São Paulo");
        p.setDestino(destino);
        p.setDescricao(descricao);
        p.setPreco(new BigDecimal(preco));
        p.setDuracaoDias(dias);
        p.setDisponivel(disponivel);
        return p;
    }
}
