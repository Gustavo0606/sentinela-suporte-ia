package br.com.sentinela.core_api.controller;

import br.com.sentinela.core_api.dto.ChamadoRequestDTO;
import br.com.sentinela.core_api.dto.ChamadoResponseDTO;
import br.com.sentinela.core_api.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {
    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService){
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> salvarChamado(@Valid @RequestBody ChamadoRequestDTO chamadoRequestDTO){
        ChamadoResponseDTO responseDTO = chamadoService.salvarChamadoInicial(chamadoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
