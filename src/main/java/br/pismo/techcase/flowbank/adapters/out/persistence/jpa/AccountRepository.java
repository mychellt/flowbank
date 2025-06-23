package br.pismo.techcase.flowbank.adapters.out.persistence.jpa;

import br.pismo.techcase.flowbank.infrastructure.persistence.model.AccountEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByDocumentNumber(final String documentNumber);
}
