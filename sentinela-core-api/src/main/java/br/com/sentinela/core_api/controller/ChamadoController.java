package br.com.sentinela.core_api.controller;

import br.com.sentinela.core_api.dto.ChamadoRequestDTO;
import br.com.sentinela.core_api.dto.ChamadoResponseDTO;
import br.com.sentinela.core_api.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/fila")
    public ResponseEntity<List<ChamadoResponseDTO>> obterFilaAtendimento() {
        return ResponseEntity.ok(chamadoService.obterFilaAtendimento());
    }

    @PatchMapping("/{id}/resolver")
    public ResponseEntity<ChamadoResponseDTO> resolverChamado(@PathVariable Long id) {
        ChamadoResponseDTO response = chamadoService.resolverChamado(id);
        return ResponseEntity.ok(response);
    }
}
