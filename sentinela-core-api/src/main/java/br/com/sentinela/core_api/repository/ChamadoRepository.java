package br.com.sentinela.core_api.repository;

import br.com.sentinela.core_api.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    @Query("SELECT c FROM Chamado c WHERE c.status IN ('TRIADO_AGUARDANDO_ATENDENTE', 'PENDENTE_FILA_COMUM') ORDER BY c.triagemIA.urgencia DESC")
    List<Chamado> findChamadosPriorizados();
}