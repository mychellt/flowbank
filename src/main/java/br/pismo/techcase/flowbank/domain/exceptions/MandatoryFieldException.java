package br.pismo.techcase.flowbank.domain.exceptions;

import br.com.fluentvalidator.context.ValidationResult;
import br.com.fluentvalidator.exception.ValidationException;

public class MandatoryFieldException extends ValidationException {
    public MandatoryFieldException(final ValidationResult validationResult) {
        super(validationResult);
    }
}
