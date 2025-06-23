package br.pismo.techcase.flowbank.adapters.in.rest.handlers;

import br.com.fluentvalidator.context.ValidationResult;
import br.com.fluentvalidator.transform.ValidationResultTransform;
import br.pismo.techcase.flowbank.domain.exceptions.AccountNotFoundException;
import br.pismo.techcase.flowbank.domain.exceptions.MandatoryFieldException;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.ArrayDeque;
import lombok.AllArgsConstructor;
import lombok.val;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Hidden
@RestControllerAdvice
public class RestControllerExceptionHandler {
    protected ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        val errorResponse = ErrorResponse.builder()
            .message(message)
            .code(status.value())
            .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MandatoryFieldException.class)
    public ResponseEntity<Object> handleMandatoryFieldException(MandatoryFieldException ex) {
        val errorResponseTransformation = new ErrorResponseTransformation(
            HttpStatus.BAD_REQUEST.value(), "Mandatory field(s) missing");
        val errorResponse = errorResponseTransformation.transform(ex.getValidationResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
        ConstraintViolationException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleInvalidRequestParams(
        MethodArgumentTypeMismatchException ex) {
        return buildErrorResponse("Invalid Parameter: " + ex.getMostSpecificCause(),
            HttpStatus.BAD_REQUEST);
    }

    @AllArgsConstructor
    static class ErrorResponseTransformation implements ValidationResultTransform<ErrorResponse> {

        private final int code;
        private final String message;

        @Override
        public ErrorResponse transform(final ValidationResult validationResult) {
            val errorResponseBuilder = ErrorResponse.builder();
            errorResponseBuilder.message(message);
            errorResponseBuilder.code(code);
            errorResponseBuilder.fields(new ArrayDeque<>());
            val errorResponse = errorResponseBuilder.build();

            for (val error : validationResult.getErrors()) {
                final ErrorField errorField = ErrorField.builder().field(error.getField())
                    .value(error.getAttemptedValue())
                    .message(error.getMessage())
                    .build();
                errorResponse.getFields().add(errorField);
            }

            return errorResponse;
        }
    }
}
