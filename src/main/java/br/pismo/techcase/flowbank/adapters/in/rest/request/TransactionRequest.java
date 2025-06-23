package br.pismo.techcase.flowbank.adapters.in.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TransactionRequest(
    @JsonProperty(value = "account_id")
    String accountId,

    @JsonProperty(value = "operation_type_id")
    Long operationTypeId,

    @JsonProperty(value = "amount")
    String amount
) { }
