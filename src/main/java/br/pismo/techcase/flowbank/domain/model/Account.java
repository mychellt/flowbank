package br.pismo.techcase.flowbank.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Account extends AbstractDomain{
    private String documentNumber;
    private BigDecimal availableCreditLimit;
}
