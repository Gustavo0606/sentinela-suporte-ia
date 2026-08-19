package br.com.sentinela.core_api.service;


import br.com.sentinela.core_api.dto.IAAnalysisResponseDTO;
import br.com.sentinela.core_api.model.*;
import br.com.sentinela.core_api.repository.ChamadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AITriagemService {
    private final ChamadoRepository chamadoRepository;
    private final RestClient restClient;

    public AITriagemService(ChamadoRepository chamadoRepository, RestClient restClient) {
        this.chamadoRepository = chamadoRepository;
        this.restClient = restClient;
    }

    @Async
    public void processarTriagem(Long chamadoId) {
        Chamado chamadoTriagem = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não existe"));
        try {
            IAAnalysisResponseDTO respostaIA = restClient.post().uri("/analyze")
                    .body(Map.of("reclamacao", chamadoTriagem.getDescricaoBruta()))
                    .retrieve().body(IAAnalysisResponseDTO.class);
            TriagemIA triagem = new TriagemIA();
            triagem.setResumo(respostaIA.resumo());
            triagem.setUrgencia(respostaIA.urgencia());
            triagem.setCategoria(Categoria.valueOf(respostaIA.categoria().toUpperCase()));
            triagem.setSentimento(Sentimento.valueOf(respostaIA.sentimento().toUpperCase()));
            triagem.setChamado(chamadoTriagem);

            chamadoTriagem.setTriagemIA(triagem);
            chamadoTriagem.setStatus(StatusChamado.TRIADO_AGUARDANDO_ATENDENTE);


        }catch (Exception e) {
            e.printStackTrace();
            chamadoTriagem.setStatus(StatusChamado.PENDENTE_FILA_COMUM);
        }
        chamadoRepository.save(chamadoTriagem);
    }
}
