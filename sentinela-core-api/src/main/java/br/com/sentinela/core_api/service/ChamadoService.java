package br.com.sentinela.core_api.service;

import br.com.sentinela.core_api.dto.ChamadoRequestDTO;
import br.com.sentinela.core_api.dto.ChamadoResponseDTO;
import br.com.sentinela.core_api.model.Chamado;
import br.com.sentinela.core_api.model.StatusChamado;
import br.com.sentinela.core_api.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final AITriagemService aiTriagemService;

    public ChamadoService(ChamadoRepository chamadoRepository, AITriagemService aiTriagemService){
        this.chamadoRepository = chamadoRepository;
        this.aiTriagemService = aiTriagemService;
    }

    public ChamadoResponseDTO salvarChamadoInicial(ChamadoRequestDTO chamadoRequestDTO){
        Chamado chamado = new Chamado();
        chamado.setDescricaoBruta(chamadoRequestDTO.descricaoBruta());
        chamado.setDataCriacao(LocalDateTime.now());
        chamado.setStatus(StatusChamado.PENDENTE_TRIAGEM);

        Chamado chamadoSalvo = chamadoRepository.save(chamado);

        aiTriagemService.processarTriagem(chamadoSalvo.getId());
        return new ChamadoResponseDTO(
                chamadoSalvo.getId(),
                chamadoSalvo.getDescricaoBruta(),
                chamadoSalvo.getStatus(),
                chamadoSalvo.getDataCriacao()
        );
    }
}
