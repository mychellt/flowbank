package br.pismo.techcase.flowbank.adapters.out.persistence;

import br.pismo.techcase.flowbank.adapters.out.persistence.jpa.OperationTypeRepository;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.ports.out.OperationTypePersistencePort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OperationTypePersistenceAdapter implements OperationTypePersistencePort {
    private final OperationTypeRepository repository;

    @Override
    public Optional<OperationType> findById(Long id) {
        return repository.findById(id).map(obj -> OperationType.fromValue(obj.getId()));
    }
}
