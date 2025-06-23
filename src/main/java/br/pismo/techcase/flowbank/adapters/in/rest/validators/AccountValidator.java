package br.pismo.techcase.flowbank.adapters.in.rest.validators;

import br.com.fluentvalidator.AbstractValidator;
import br.pismo.techcase.flowbank.adapters.in.rest.request.AccountRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator extends AbstractValidator<AccountRequest> {

    @Override
    public void rules() {
        ruleFor(AccountRequest::documentNumber)
            .must(documentNumber -> documentNumber != null && !documentNumber.isBlank())
            .withMessage("Document Nummber must not be null or empty");
    }
}
