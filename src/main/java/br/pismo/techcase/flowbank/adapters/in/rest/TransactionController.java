package br.pismo.techcase.flowbank.adapters.in.rest;

import br.pismo.techcase.flowbank.adapters.in.rest.request.TransactionRequest;
import br.pismo.techcase.flowbank.adapters.in.rest.validators.TransactionValidator;
import br.pismo.techcase.flowbank.application.service.TransactionService;
import br.pismo.techcase.flowbank.domain.exceptions.MandatoryFieldException;
import br.pismo.techcase.flowbank.domain.model.Account;
import br.pismo.techcase.flowbank.domain.model.OperationType;
import br.pismo.techcase.flowbank.domain.model.Transaction;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Transactions", description = "Operations related to transactions")
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionValidator validator;

    @PostMapping
    @Operation(
        summary = "Create a new transaction",
        description = "Creates a new transaction for a given account.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Transaction creation request",
            required = true,
            content = @Content(
                schema = @Schema(implementation = TransactionRequest.class)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
        }
    )
    public ResponseEntity<Void> createTransaction(@RequestBody final TransactionRequest payload) {

        validator.validate(payload).isInvalidThrow(MandatoryFieldException.class);

        var transaction = transactionService.createTransaction(Transaction.builder()
            .account(Account.builder()
                .id(UUID.fromString(payload.accountId()))
                .build())
            .amount(new BigDecimal(payload.amount()))
            .operationType(OperationType.fromValue(payload.operationTypeId()))
            .eventDate(LocalDateTime.now())
            .build());

        return ResponseEntity.created(URI.create("/transactions/" + transaction.getId())).build();
    }
}
