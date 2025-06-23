package br.pismo.techcase.flowbank.adapters.out.persistence;

import static java.util.Objects.nonNull;
import static java.util.Optional.of;

import br.pismo.techcase.flowbank.adapters.out.mappers.TransactionMapper;
import br.pismo.techcase.flowbank.adapters.out.persistence.jpa.AccountRepository;
import br.pismo.techcase.flowbank.adapters.out.persistence.jpa.OperationTypeRepository;
import br.pismo.techcase.flowbank.adapters.out.persistence.jpa.TransactionRepository;
import br.pismo.techcase.flowbank.domain.exceptions.AccountNotFoundException;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import br.pismo.techcase.flowbank.domain.ports.out.TransactionPersistencePort;
import br.pismo.techcase.flowbank.infrastructure.persistence.model.TransactionEntity;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionPersistencePort {
    private final TransactionRepository repository;
    private final OperationTypeRepository operationTypeRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper mapper;

    @Override
    public Transaction save(final Transaction transaction) {
        var entity = mapper.map(transaction);

        entity.setOperationType(
            operationTypeRepository.findById(transaction.getOperationType().getId())
                .orElseThrow(() -> new RuntimeException("Operation type not found")));

        entity.setAccount(accountRepository.findById(transaction.getAccount().getId())
            .orElseThrow(AccountNotFoundException::new));

        return of(repository.save(entity))
           .map(mapper::map)
           .orElseThrow(() -> new RuntimeException("Failed to save transaction"));
    }

    @Override
    public Optional<Transaction> findById(final UUID id) {
        return repository.findById(id).map(mapper::map);
    }

    @Override
    public Page<Transaction> findAll(final UUID accountId,
        final OperationType operationType,
        final LocalDateTime from,
        final LocalDateTime to,
        final Pageable pageable) {

        Specification<TransactionEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nonNull(accountId)) {
                predicates.add(criteriaBuilder.equal(root.get("account").get("id"), accountId));
            }

            if (nonNull(operationType)) {
                predicates.add(criteriaBuilder.equal(root.get("operationType").get("id"), operationType.getId()));
            }

            if (nonNull(from) && nonNull(to)) {
                predicates.add(criteriaBuilder.between(root.get("eventDate"), from, to));
            } else if (nonNull(from)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), from));
            } else if (nonNull(to)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), to));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(specification, pageable)
            .map(mapper::map);
    }
}
