package br.pismo.techcase.flowbank.adapters.out.persistence;

import br.pismo.techcase.flowbank.infrastructure.persistence.model.OperationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {

}
