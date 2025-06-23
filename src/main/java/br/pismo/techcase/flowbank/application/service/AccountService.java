package br.pismo.techcase.flowbank.application.service;

import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.ports.in.CreateAccountUseCase;
import br.pismo.techcase.flowbank.domain.ports.in.FetchAccountUseCase;
import br.pismo.techcase.flowbank.domain.ports.out.AccountPersistencePort;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase, FetchAccountUseCase {
    private final AccountPersistencePort accountPersistencePort;

    @Override
    public Account createAccount(final String documentNumber) {
        return accountPersistencePort.save(Account.builder()
                .createdAt(LocalDateTime.now())
                .documentNumber(documentNumber)
            .build());
    }

    @Override
    public Optional<Account> fetch(final UUID id) {
        return accountPersistencePort.findById(id);
    }
}
