package br.pismo.techcase.flowbank.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractDomain {
    private UUID id;
    private boolean active;
    private LocalDateTime createdAt;
}
