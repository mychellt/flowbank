package br.pismo.techcase.flowbank.adapters.in.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TransactionResponse(
    @JsonProperty(value = "document_number")
    String documentNumber,

    @JsonProperty(value = "operation_type")
    String operationType,

    @JsonProperty(value = "amount")
    String amount,

    @JsonProperty(value = "event_date")
    String eventDate){
}
