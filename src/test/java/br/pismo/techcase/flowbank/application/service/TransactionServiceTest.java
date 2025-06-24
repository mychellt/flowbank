package br.pismo.techcase.flowbank.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import br.pismo.techcase.flowbank.domain.ports.out.AccountPersistencePort;
import br.pismo.techcase.flowbank.domain.ports.out.TransactionPersistencePort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionPersistencePort transactionPersistencePort;

    @Mock
    private AccountPersistencePort accountPersistencePort;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    @DisplayName(value = "Given a transaction, createTransaction returns saved transaction")
    void createTransactionReturnsSavedTransaction() {

        var account = Account.builder()
            .availableCreditLimit(BigDecimal.valueOf(100))
            .id(UUID.randomUUID())
            .documentNumber("19090")
            .build();

        var transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .operationType(OperationType.PURCHASE_WITH_INSTALLMENTS)
                .eventDate(LocalDateTime.now())
                .account(account)
                .build();

        when(accountPersistencePort.findById(any(UUID.class))).thenReturn(Optional.of(account));

        when(transactionPersistencePort.save(any(Transaction.class))).thenReturn(transaction);
        var result = transactionService.createTransaction(transaction);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(transaction.getId());
    }

    @Test
    @DisplayName(value = "Given a transaction, createTransaction returns null when transaction is null")
    void getByAccountIdReturnsPagedTransactions() {
        var accountId = UUID.randomUUID();
        var opType = OperationType.PURCHASE_WITH_INSTALLMENTS;
        var from = LocalDateTime.now().minusDays(1);
        var to = LocalDateTime.now();
        var pageable = PageRequest.of(0, 10);
        var transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .operationType(opType)
                .eventDate(from.plusHours(1))
                .build();
        var page = new PageImpl<>(List.of(transaction), pageable, 1);

        when(transactionPersistencePort.findAll(eq(accountId), eq(opType), eq(from), eq(to), eq(pageable))).thenReturn(page);

        var result = transactionService.getByAccountId(accountId, opType, from, to, pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getOperationType()).isEqualTo(opType);
    }

    @Test
    @DisplayName(value = "Given a Account ID, OperationType, from and to dates, getByAccountId returns empty page when no transactions")
    void getByAccountIdReturnsEmptyPageWhenNoTransactions() {
        var accountId = UUID.randomUUID();
        var operationType = OperationType.PURCHASE_WITH_INSTALLMENTS;
        var from = LocalDateTime.now().minusDays(1);
        var to = LocalDateTime.now();
        var pageable = PageRequest.of(0, 10);
        Page<Transaction> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(transactionPersistencePort.findAll(eq(accountId), eq(operationType), eq(from), eq(to), eq(pageable))).thenReturn(emptyPage);

        var result = transactionService.getByAccountId(accountId, operationType, from, to, pageable);
        assertThat(result.getContent()).isEmpty();
    }
}
