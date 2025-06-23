package br.pismo.techcase.flowbank.adapters.in.rest;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.pismo.techcase.flowbank.adapters.in.rest.handlers.RestControllerExceptionHandler;
import br.pismo.techcase.flowbank.adapters.in.rest.validators.AccountValidator;
import br.pismo.techcase.flowbank.application.service.AccountService;
import br.pismo.techcase.flowbank.application.service.TransactionService;
import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Spy
    private AccountValidator validator;

    private UUID accountId;

    private Account account;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new RestControllerExceptionHandler())
            .build();
        accountId = UUID.randomUUID();
        account = Account.builder()
                .id(accountId)
                .documentNumber("12345678900")
                .build();
    }

    @DisplayName(value = "Given a valid document number, when Create Account is called, then it should return 201 Created with Location header")
    @Test
    void createAccountReturnsCreatedStatusAndLocationHeader() throws Exception {
        when(accountService.createAccount("12345678900")).thenReturn(account);
        var payload = "{\"document_number\":\"12345678900\"}";
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string("Location", "/accounts/" + accountId));
    }

    @DisplayName(value = "Given a valid document number, when Get Account is called, then it should return the account details")
    @Test
    void getAccountReturnsAccountDetailsWhenFound() throws Exception {
        when(accountService.fetch(accountId)).thenReturn(Optional.of(account));
        mockMvc.perform(get("/accounts/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_id").value(accountId.toString()))
                .andExpect(jsonPath("$.document_number").value("12345678900"));
    }

    @DisplayName(value = "Given a non-existing account ID, when Get Account is called, then it should return 400 Bad Request")
    @Test
    void getAccountReturnsNotFoundWhenAccountDoesNotExist() throws Exception {
        when(accountService.fetch(accountId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/accounts/" + accountId))
                .andExpect(status().isBadRequest());
    }

    @DisplayName(value = "Given a valid account ID, when Get Transactions is called, then it should return paginated transactions")
    @Test
    void getTransactionsByAccountReturnsPaginatedTransactions() throws Exception {
        var from = LocalDateTime.now().minusDays(1);
        var to = LocalDateTime.now();
        var operationType = OperationType.PURCHASE_WITH_INSTALLMENTS;
        Transaction transaction = Transaction.builder()
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .eventDate(from.plusHours(1))
                .operationType(operationType)
                .build();
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(List.of(transaction), pageable, 1);
        when(transactionService.getByAccountId(eq(accountId), eq(operationType), eq(from), eq(to), any(Pageable.class)))
                .thenReturn(page);
        mockMvc.perform(get("/accounts/" + accountId + "/transactions")
                .param("operationType", String.valueOf(operationType.getId()))
                .param("from", from.toString())
                .param("to", to.toString())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].document_number").value("12345678900"))
                .andExpect(jsonPath("$.content[0].amount").value("100"))
                .andExpect(jsonPath("$.content[0].operation_type").value(operationType.getDescription()));
    }

    @DisplayName(value = "Given a valid account ID and no transactions, when Get Transactions is called, then it should return an empty page")
    @Test
    void getTransactionsByAccountReturnsEmptyPageWhenNoTransactions() throws Exception {
        var from = LocalDateTime.now().minusDays(1);
        var to = LocalDateTime.now();
        var operationType = OperationType.PURCHASE_WITH_INSTALLMENTS;
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(transactionService.getByAccountId(eq(accountId), eq(operationType), eq(from), eq(to), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));
        mockMvc.perform(get("/accounts/" + accountId + "/transactions")
                .param("operationType", String.valueOf(operationType.getId()))
                .param("from", from.toString())
                .param("to", to.toString())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }
}

