package br.pismo.techcase.flowbank.adapters.out.mappers;

import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import br.pismo.techcase.flowbank.infrastructure.persistence.model.TransactionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "operationType", ignore = true)
    Transaction map(final TransactionEntity source);

    @Mapping(target = "operationType", ignore = true)
    TransactionEntity map(final Transaction source);

    @AfterMapping
    default void after(@MappingTarget Transaction transaction, TransactionEntity source) {
        transaction.setOperationType(OperationType.fromValue(source.getOperationType().getId()));
    }
}
