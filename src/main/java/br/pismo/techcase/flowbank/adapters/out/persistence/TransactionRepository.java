package br.pismo.techcase.flowbank.adapters.out.persistence;

import br.pismo.techcase.flowbank.domain.model.Transaction;
import br.pismo.techcase.flowbank.infrastructure.persistence.model.TransactionEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID>,
    JpaSpecificationExecutor<TransactionEntity> {
}
