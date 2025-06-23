package br.pismo.techcase.flowbank.adapters.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.pismo.techcase.flowbank.adapters.in.rest.handlers.RestControllerExceptionHandler;
import br.pismo.techcase.flowbank.adapters.in.rest.validators.TransactionValidator;
import br.pismo.techcase.flowbank.application.service.TransactionService;
import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @Spy
    private TransactionValidator validator;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
            .setControllerAdvice(new RestControllerExceptionHandler())
            .build();
    }

    @Test
    @DisplayName(value = "Returns 201 Created and Location header when input is valid")
    void createTransactionReturnsCreatedAndLocation() throws Exception {
        var accountId = UUID.randomUUID();
        var transactionId = UUID.randomUUID();
        var transaction = Transaction.builder()
                .id(transactionId)
                .account(Account.builder().id(accountId).build())
                .amount(new BigDecimal("100.00"))
                .operationType(OperationType.fromValue(1L))
                .eventDate(LocalDateTime.now())
                .build();
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);
        var payload = String.format("{\"account_id\":\"%s\",\"amount\":\"100.00\",\"operation_type_id\":1}", accountId);
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/transactions/" + transactionId));
    }

    @Test
    @DisplayName(value = "Returns 400 Bad Request when input is invalid")
    void createTransactionReturnsBadRequestOnInvalidInput() throws Exception {
        var payload = "{\"account_id\":\"not-a-uuid\",\"amount\":\"abc\",\"operation_type_id\":null}";
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "Returns 400 Bad Request when required fields are missing")
    void createTransactionReturnsBadRequestWhenFieldsMissing() throws Exception {
        var payload = "{}";
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 400 Bad Request when operationTypeId is invalid")
    void createTransactionReturnsBadRequestWhenOperationTypeIdInvalid() throws Exception {
        var accountId = UUID.randomUUID();
        var payload = String.format("{\"accountId\":\"%s\",\"amount\":\"100.00\",\"operationTypeId\":999}", accountId);
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }
}
