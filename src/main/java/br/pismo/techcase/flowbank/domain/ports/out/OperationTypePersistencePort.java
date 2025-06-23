package br.pismo.techcase.flowbank.domain.ports.out;

import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import java.util.Optional;
import java.util.UUID;

public interface OperationTypePersistencePort {
    Optional<OperationType> findById(final Long id);
}
