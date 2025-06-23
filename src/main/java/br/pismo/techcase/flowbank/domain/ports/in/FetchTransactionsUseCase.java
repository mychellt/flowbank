package br.pismo.techcase.flowbank.domain.ports.in;

import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FetchTransactionsUseCase {
    Page<Transaction> getByAccountId(final UUID accountId,
        final OperationType operationType,
        final LocalDateTime from,
        final LocalDateTime to,
        final Pageable pageable);
}
