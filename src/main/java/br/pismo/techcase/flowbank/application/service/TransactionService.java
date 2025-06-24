package br.pismo.techcase.flowbank.application.service;

import br.pismo.techcase.flowbank.domain.exceptions.AccountNotFoundException;
import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import br.pismo.techcase.flowbank.domain.ports.in.CreateTransactionUseCase;
import br.pismo.techcase.flowbank.domain.ports.in.FetchTransactionsUseCase;
import br.pismo.techcase.flowbank.domain.ports.out.AccountPersistencePort;
import br.pismo.techcase.flowbank.domain.ports.out.TransactionPersistencePort;
import jakarta.transaction.Transactional;
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
    private final AccountPersistencePort accountPersistencePort;

    @Transactional(rollbackOn = IllegalArgumentException.class)
    @Override
    public Transaction createTransaction(final Transaction transaction) {

        var account = accountPersistencePort.findById(transaction.getAccount().getId())
            .orElseThrow(AccountNotFoundException::new);

        if(transaction.isDebit()) {
            if(account.getAvailableCreditLimit().compareTo(transaction.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient credit limit for transaction");
            }
            account.setAvailableCreditLimit(account.getAvailableCreditLimit().subtract(transaction.getAmount()));
        }
        else {
            account.setAvailableCreditLimit(account.getAvailableCreditLimit().add(transaction.getAmount()));
        }

        accountPersistencePort.save(account);
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
