package br.pismo.techcase.flowbank.domain.ports.in;

import br.pismo.techcase.flowbank.domain.model.Account;
import java.util.Optional;
import java.util.UUID;

public interface FetchAccountUseCase {
    Optional<Account> fetch(final UUID id);
}
