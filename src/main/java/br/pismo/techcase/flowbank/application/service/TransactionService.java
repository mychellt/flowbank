package br.pismo.techcase.flowbank.application.service;

import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import br.pismo.techcase.flowbank.domain.ports.in.CreateTransactionUseCase;
import br.pismo.techcase.flowbank.domain.ports.in.FetchTransactionsUseCase;
import br.pismo.techcase.flowbank.domain.ports.out.TransactionPersistencePort;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService implements CreateTransactionUseCase, FetchTransactionsUseCase {
    private final TransactionPersistencePort transactionPersistencePort;

    @Override
    public Transaction createTransaction(final Transaction transaction) {
        return transactionPersistencePort.save(transaction);
    }

    @Override
    public Page<Transaction> getByAccountId(final UUID accountId,
        final OperationType operationType,
        final LocalDateTime from,
        final LocalDateTime to,
        final Pageable pageable) {
        return transactionPersistencePort.findAll(accountId, operationType, from, to, pageable);
    }
}
