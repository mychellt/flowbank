package br.pismo.techcase.flowbank.adapters.in.rest;

import br.pismo.techcase.flowbank.adapters.in.rest.request.AccountRequest;
import br.pismo.techcase.flowbank.adapters.in.rest.response.AccountResponse;
import br.pismo.techcase.flowbank.adapters.in.rest.response.TransactionResponse;
import br.pismo.techcase.flowbank.adapters.in.rest.validators.AccountValidator;
import br.pismo.techcase.flowbank.application.service.AccountService;
import br.pismo.techcase.flowbank.application.service.TransactionService;
import br.pismo.techcase.flowbank.domain.exceptions.AccountNotFoundException;
import br.pismo.techcase.flowbank.domain.exceptions.MandatoryFieldException;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Accounts", description = "Operations related to accounts and their transactions")
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AccountValidator validator;

    @Operation(
        summary = "Create a new account",
        description = "Creates a new account with the provided document number.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Account creation request",
            required = true,
            content = @Content(
                schema = @Schema(implementation = AccountRequest.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Account created")
        }
    )
    @PostMapping
    public ResponseEntity<Void> createAccount(@RequestBody AccountRequest payload) {
        validator.validate(payload).isInvalidThrow(MandatoryFieldException.class);

        var account = accountService.createAccount(payload.documentNumber());
        return ResponseEntity.created(URI.create("/accounts/" + account.getId())).build();
    }

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves account details by account ID.",
        parameters = {
            @Parameter(name = "accountId", description = "UUID of the account", required = true)
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Account found",
                content = @Content(schema = @Schema(implementation = AccountResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Account not found")
        }
    )
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable(name = "accountId") final UUID accountId) {
        var account = accountService.fetch(accountId)
            .orElseThrow(AccountNotFoundException::new);

        return ResponseEntity.ok(AccountResponse.builder()
            .documentNumber(account.getDocumentNumber())
            .id(account.getId().toString())
            .build());
    }

    @Operation(
        summary = "Get transactions by account",
        description = "Retrieves a paginated list of transactions for a given account, filtered by operation type and date range.",
        parameters = {
            @Parameter(name = "accountId", description = "UUID of the account", required = true),
            @Parameter(name = "operationType", description = "Operation type (numeric value)", required = true),
            @Parameter(name = "from", description = "Start date-time (ISO format)", required = true, example = "2024-01-01T00:00:00"),
            @Parameter(name = "to", description = "End date-time (ISO format)", required = true, example = "2024-01-31T23:59:59")
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of transactions",
                content = @Content(schema = @Schema(implementation = TransactionResponse.class, type = "array"))
            )
        }
    )
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByAccount(
        @PathVariable String accountId,
        @RequestParam String operationType,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(defaultValue = "0") Long page,
        @RequestParam(defaultValue = "30") Long size
    ) {
        var pageRequest = PageRequest.of(page.intValue(), size.intValue());
        Page<Transaction> transactions = transactionService.getByAccountId(UUID.fromString(accountId),
            OperationType.fromValue(Long.valueOf(operationType)), from, to, pageRequest);

        Page<TransactionResponse> response =  transactions.map(transaction -> TransactionResponse.builder()
            .documentNumber(transaction.getAccount().getDocumentNumber())
            .amount(transaction.getAmount().toEngineeringString())
            .eventDate(transaction.getEventDate().toString())
            .operationType(transaction.getOperationType().getDescription())
            .build());
        return ResponseEntity.ok(response);
    }

}
