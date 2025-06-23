package br.pismo.techcase.flowbank.domain.ports.in;

import br.pismo.techcase.flowbank.domain.model.Transaction;

public interface CreateTransactionUseCase {
    Transaction createTransaction(final Transaction transaction);
}
