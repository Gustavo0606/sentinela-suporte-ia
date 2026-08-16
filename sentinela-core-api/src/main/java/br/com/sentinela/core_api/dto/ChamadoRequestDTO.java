package br.com.sentinela.core_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChamadoRequestDTO(
        @NotBlank(message = "A descrição do chamado não pode estar em branco.")
        @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres.")
        String descricaoBruta
) {
}
