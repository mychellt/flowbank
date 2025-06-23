package br.pismo.techcase.flowbank.domain.ports.out;

import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionPersistencePort {
    Transaction save(final Transaction transaction);
    Optional<Transaction> findById(final UUID id);
    Page<Transaction> findAll(final UUID accountId,
        final OperationType operationType,
        final LocalDateTime from,
        final LocalDateTime to,
        final Pageable pageable);
}
