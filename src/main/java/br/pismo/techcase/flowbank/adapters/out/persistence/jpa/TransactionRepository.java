package br.pismo.techcase.flowbank.adapters.out.persistence.jpa;

import br.pismo.techcase.flowbank.infrastructure.persistence.model.TransactionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID>,
    JpaSpecificationExecutor<TransactionEntity> {
}
