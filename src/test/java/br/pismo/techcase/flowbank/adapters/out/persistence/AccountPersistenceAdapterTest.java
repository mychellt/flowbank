package br.pismo.techcase.flowbank.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import br.pismo.techcase.flowbank.adapters.out.mappers.AccountMapper;
import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.infrastructure.persistence.model.AccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountPersistenceAdapterTest {
    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountPersistenceAdapter adapter;

    @Test
    @DisplayName(value = "Save returns mapped account when repository save succeeds")
    void saveReturnsMappedAccountWhenRepositorySaveSucceeds() {
        Account domainAccount = Account.builder().id(UUID.randomUUID()).documentNumber("123").build();
        AccountEntity entity = new AccountEntity();
        when(mapper.map(domainAccount)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(domainAccount);
        Account result = adapter.save(domainAccount);
        assertThat(result).isEqualTo(domainAccount);
    }

    @Test
    @DisplayName(value = "Save throws exception when repository save returns null")
    void saveThrowsExceptionWhenRepositorySaveReturnsNull() {
        var domainAccount = Account.builder().id(UUID.randomUUID()).documentNumber("123").build();
        when(mapper.map(domainAccount)).thenReturn(null);
        when(repository.save(null)).thenReturn(null);
        assertThatThrownBy(() -> adapter.save(domainAccount)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName(value = "FindById returns mapped account when found")
    void findByIdReturnsMappedAccountWhenFound() {
        var id = UUID.randomUUID();
        var entity = new AccountEntity();
        var domainAccount = Account.builder().id(id).documentNumber("123").build();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.map(entity)).thenReturn(domainAccount);
        Optional<Account> result = adapter.findById(id);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domainAccount);
    }

    @Test
    @DisplayName(value = "FindById returns empty when not found")
    void findByIdReturnsEmptyWhenNotFound() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Account> result = adapter.findById(id);
        assertThat(result).isEmpty();
    }
}

