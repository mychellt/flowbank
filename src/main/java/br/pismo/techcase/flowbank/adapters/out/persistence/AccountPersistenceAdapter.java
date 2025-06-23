package br.pismo.techcase.flowbank.adapters.out.persistence;

import static java.util.Optional.of;

import br.pismo.techcase.flowbank.adapters.out.mappers.AccountMapper;
import br.pismo.techcase.flowbank.adapters.out.persistence.jpa.AccountRepository;
import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.ports.out.AccountPersistencePort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPersistencePort {
    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public Account save(final Account account) {
       return of(repository.save(mapper.map(account)))
           .map(mapper::map)
           .orElseThrow(() -> new RuntimeException("Failed to save account"));
    }

    @Override
    public Optional<Account> findById(final UUID id) {
        return repository.findById(id).map(mapper::map);
    }
}
