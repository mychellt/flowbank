package br.pismo.techcase.flowbank.adapters.in.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AccountRequest(
    @JsonProperty("document_number")
    String documentNumber
) {  }
