package br.pismo.techcase.flowbank.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.ports.out.AccountPersistencePort;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountPersistencePort accountPersistencePort;

    @InjectMocks
    private AccountService accountService;


    @Test
    @DisplayName(value = "Returns saved account with correct document number")
    void createAccountReturnsSavedAccount() {
        var documentNumber = "12345678900";
        var saved = Account.builder()
            .id(UUID.randomUUID())
            .documentNumber(documentNumber)
            .createdAt(LocalDateTime.now())
            .build();

        when(accountPersistencePort.save(any(Account.class))).thenReturn(saved);

        var result = accountService.createAccount(documentNumber);

        assertThat(result).isNotNull();
        assertThat(result.getDocumentNumber()).isEqualTo(documentNumber);
    }

    @Test
    @DisplayName(value = "Fetch returns account when found")
    void fetchReturnsAccountWhenFound() {
        var id = UUID.randomUUID();
        var account = Account.builder().id(id).documentNumber("123").createdAt(LocalDateTime.now()).build();

        when(accountPersistencePort.findById(id)).thenReturn(Optional.of(account));

        var result = accountService.fetch(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName(value = "Fetch returns empty when account not found")
    void fetchReturnsEmptyWhenNotFound() {
        var id = UUID.randomUUID();
        when(accountPersistencePort.findById(id)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.fetch(id);

        assertThat(result).isEmpty();
    }
}

