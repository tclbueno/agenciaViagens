package com.agencia.viagens.aerea;

import com.agencia.viagens.common.exception.RecursoNaoEncontradoException;
import com.agencia.viagens.common.exception.RegraDeNegocioException;
import com.agencia.viagens.common.exception.ServicoIndisponivelException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

/** Comunicação HTTP com a API simulada da companhia aérea (projeto separado). */
@Component
public class CompanhiaAereaClient {

    private final RestClient rest;

    public CompanhiaAereaClient(RestClient.Builder builder, @Value("${aerea.url}") String baseUrl) {
        this.rest = builder.baseUrl(baseUrl).build();
    }

    public List<VooDTO> listar(String origem, String destino, LocalDate data) {
        try {
            return rest.get()
                    .uri(u -> {
                        var b = u.path("/voos");
                        if (origem != null) b = b.queryParam("origem", origem);
                        if (destino != null) b = b.queryParam("destino", destino);
                        if (data != null) b = b.queryParam("data", data);
                        return b.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<VooDTO>>() {});
        } catch (ResourceAccessException e) {
            throw indisponivel();
        }
    }

    public VooDTO buscar(Long id) {
        try {
            return rest.get().uri("/voos/{id}", id).retrieve().body(VooDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNaoEncontradoException("Voo " + id + " não encontrado na companhia aérea");
        } catch (ResourceAccessException e) {
            throw indisponivel();
        }
    }

    public VooDTO reservarAssentos(Long id, int quantidade) {
        try {
            return rest.post().uri("/voos/{id}/reservar-assentos", id)
                    .body(new AssentosRequest(quantidade)).retrieve().body(VooDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNaoEncontradoException("Voo " + id + " não encontrado na companhia aérea");
        } catch (HttpClientErrorException.Conflict e) {
            throw new RegraDeNegocioException("Voo sem assentos suficientes");
        } catch (ResourceAccessException e) {
            throw indisponivel();
        }
    }

    public VooDTO liberarAssentos(Long id, int quantidade) {
        try {
            return rest.post().uri("/voos/{id}/liberar-assentos", id)
                    .body(new AssentosRequest(quantidade)).retrieve().body(VooDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNaoEncontradoException("Voo " + id + " não encontrado na companhia aérea");
        } catch (ResourceAccessException e) {
            throw indisponivel();
        }
    }

    private ServicoIndisponivelException indisponivel() {
        return new ServicoIndisponivelException("API da companhia aérea indisponível");
    }
}
