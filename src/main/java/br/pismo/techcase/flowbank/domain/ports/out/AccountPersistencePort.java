package br.pismo.techcase.flowbank.domain.ports.out;

import br.pismo.techcase.flowbank.domain.model.Account;
import java.util.Optional;
import java.util.UUID;

public interface AccountPersistencePort {
    Account save(final Account account);
    Optional<Account> findById(final UUID id);
}
