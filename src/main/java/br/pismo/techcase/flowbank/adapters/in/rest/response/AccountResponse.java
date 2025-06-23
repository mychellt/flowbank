package br.pismo.techcase.flowbank.adapters.in.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AccountResponse(
    @JsonProperty(value = "account_id")
    String id,

    @JsonProperty(value = "document_number")
    String documentNumber
) {
}
