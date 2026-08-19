package br.com.sentinela.core_api.repository;

import br.com.sentinela.core_api.model.Chamado;
import br.com.sentinela.core_api.model.StatusChamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    List<Chamado> findByStatusInOrderByScorePrioridadeDesc(List<StatusChamado> statuses);
}