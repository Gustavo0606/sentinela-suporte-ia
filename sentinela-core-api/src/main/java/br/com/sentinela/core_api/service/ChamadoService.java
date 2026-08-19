package br.com.sentinela.core_api.service;

import br.com.sentinela.core_api.dto.ChamadoRequestDTO;
import br.com.sentinela.core_api.dto.ChamadoResponseDTO;
import br.com.sentinela.core_api.model.Chamado;
import br.com.sentinela.core_api.model.StatusChamado;
import br.com.sentinela.core_api.repository.ChamadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                chamadoSalvo.getDataCriacao(),
                chamadoSalvo.getScorePrioridade()
        );
    }

    public List<ChamadoResponseDTO> obterFilaAtendimento() {
        List<StatusChamado> statusAtendimento = List.of(
                StatusChamado.TRIADO_AGUARDANDO_ATENDENTE,
                StatusChamado.PENDENTE_FILA_COMUM
        );

        return chamadoRepository.findByStatusInOrderByScorePrioridadeDesc(statusAtendimento)
                .stream()
                .map(c -> new ChamadoResponseDTO(c.getId(), c.getDescricaoBruta(), c.getStatus(), c.getDataCriacao(), c.getScorePrioridade()))
                .toList();
    }

    public ChamadoResponseDTO resolverChamado(Long id){
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado"));
        if (chamado.getStatus() == StatusChamado.RESOLVIDO){
            throw new IllegalStateException("Esse chamado já está fechado");
        }

        chamado.setStatus(StatusChamado.RESOLVIDO);
        chamado.setDataResolucao(LocalDateTime.now());

        Chamado chamadoAtualizado = chamadoRepository.save(chamado);

        return new ChamadoResponseDTO(
                chamadoAtualizado.getId(),
                chamadoAtualizado.getDescricaoBruta(),
                chamadoAtualizado.getStatus(),
                chamadoAtualizado.getDataCriacao(),
                chamadoAtualizado.getScorePrioridade()
        );
    }
}
