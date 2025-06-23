package br.pismo.techcase.flowbank.adapters.in.rest.validators;

import static org.assertj.core.api.Assertions.assertThat;

import br.pismo.techcase.flowbank.adapters.in.rest.request.AccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountValidatorTest {
    private AccountValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AccountValidator();
    }

    @Test
    @DisplayName(value = "Should be valid when document number is present and not blank")
    void validWhenDocumentNumberPresent() {
        AccountRequest request = new AccountRequest("12345678900");
        var result = validator.validate(request);
        assertThat(result.isValid()).isTrue();
    }

    @Test
    @DisplayName(value = "Should be invalid when document number is null")
    void invalidWhenDocumentNumberNull() {
        AccountRequest request = new AccountRequest(null);
        var result = validator.validate(request);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()
            .stream()
            .findFirst()
            .get()
            .getMessage())
            .containsIgnoringCase("must not be null or empty");
    }

    @Test
    @DisplayName(value = "Should be invalid when document number is blank")
    void invalidWhenDocumentNumberBlank() {
        AccountRequest request = new AccountRequest("");
        var result = validator.validate(request);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()
            .stream()
            .findFirst()
            .get()
            .getMessage())
            .containsIgnoringCase("must not be null or empty");
    }

    @Test
    @DisplayName(value = "Should be invalid when document number is only whitespace")
    void invalidWhenDocumentNumberWhitespace() {
        AccountRequest request = new AccountRequest("   ");
        var result = validator.validate(request);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()
            .stream()
            .findFirst()
            .get()
            .getMessage())
            .containsIgnoringCase("must not be null or empty");
    }
}

