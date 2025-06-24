package br.pismo.techcase.flowbank.domain.model;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationType {
    NORMAL_PURCHASE(1L, "Normal Purchase", Boolean.TRUE),
    PURCHASE_WITH_INSTALLMENTS(2L,  "purchase_with_installments", Boolean.TRUE),
    WITHDRAWAL(3L, "Withdrawal", Boolean.TRUE),
    CREDIT_VOUCHER(4L, "Credit Voucher", Boolean.FALSE),;

    private final Long id;
    private final String description;
    private final boolean debit;

    private static final Map<Long, OperationType> MAP_STRING = new HashMap<>();

    static {
        for (OperationType type : values()) {
            MAP_STRING.put(type.getId(), type);
        }
    }

    public static OperationType fromValue(final Long id) {
        return id != null ? MAP_STRING.get(id) : null;
    }

}
