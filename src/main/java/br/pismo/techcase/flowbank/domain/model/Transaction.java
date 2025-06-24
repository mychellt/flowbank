package br.pismo.techcase.flowbank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
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
public class Transaction extends AbstractDomain {
    private Account account;
    private OperationType operationType;
    private BigDecimal amount;
    private LocalDateTime eventDate;

    public boolean isDebit() {
        return Optional.ofNullable(operationType)
            .map(OperationType::isDebit)
            .orElse(false);
    }
}
