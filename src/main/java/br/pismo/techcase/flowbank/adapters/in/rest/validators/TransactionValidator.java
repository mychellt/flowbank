package br.pismo.techcase.flowbank.adapters.in.rest.validators;

import br.com.fluentvalidator.AbstractValidator;
import br.pismo.techcase.flowbank.adapters.in.rest.request.TransactionRequest;
import org.springframework.stereotype.Component;


@Component
public class TransactionValidator extends AbstractValidator<TransactionRequest> {

    @Override
    public void rules() {
        ruleFor(TransactionRequest::accountId)
            .must(accountId -> accountId != null && !accountId.isBlank())
            .withMessage("Account ID must not be null or empty");

        ruleFor(TransactionRequest::operationTypeId)
            .must(operationTypeId -> operationTypeId != null && operationTypeId > 0)
            .withMessage("Operation Type ID must be a positive number");

        ruleFor(TransactionRequest::amount)
            .must(amount -> amount != null && !amount.isBlank() && amount.matches("-?\\d+(\\.\\d+)?"))
            .withMessage("Amount must be a valid number");
    }

}
