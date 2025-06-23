package br.pismo.techcase.flowbank.domain.ports.in;

import br.pismo.techcase.flowbank.domain.model.Account;

public interface CreateAccountUseCase {
    Account createAccount(final String documentNumber);
}
