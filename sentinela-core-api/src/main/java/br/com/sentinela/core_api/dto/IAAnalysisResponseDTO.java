package br.com.sentinela.core_api.dto;

public record IAAnalysisResponseDTO(
        String sentimento,
        String categoria,
        Integer urgencia,
        String resumo
) {
}
