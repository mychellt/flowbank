CREATE TABLE accounts (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      document_number VARCHAR(255) NOT NULL UNIQUE,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE operation_types (
     id BIGSERIAL PRIMARY KEY,
     description VARCHAR(255) NOT NULL UNIQUE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE transactions (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      operation_type_id BIGINT NOT NULL,
      amount NUMERIC(19, 2) NOT NULL,
      event_date TIMESTAMP NOT NULL,
      account_id UUID NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      active BOOLEAN NOT NULL DEFAULT TRUE,
      CONSTRAINT fk_transaction_operation_type FOREIGN KEY (operation_type_id) REFERENCES operation_types(id),
      CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

INSERT INTO operation_types (id, description) VALUES
         (1, 'Normal Purchase'),
         (2, 'purchase_with_installments'),
         (3, 'Withdrawal'),
         (4, 'Credit Voucher');