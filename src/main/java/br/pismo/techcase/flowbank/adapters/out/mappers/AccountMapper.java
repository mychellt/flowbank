package br.pismo.techcase.flowbank.adapters.out.mappers;

import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.infrastructure.persistence.model.AccountEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface AccountMapper {
    Account map(final AccountEntity source);
    AccountEntity map(final Account source);
}
