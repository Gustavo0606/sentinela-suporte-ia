package br.com.sentinela.core_api.dto;

import br.com.sentinela.core_api.model.StatusChamado;
import java.time.LocalDateTime;

public record ChamadoResponseDTO(
        Long id,
        String descricaoBruta,
        StatusChamado status,
        LocalDateTime dataCriacao,
        Integer scorePrioridade
) {
}
