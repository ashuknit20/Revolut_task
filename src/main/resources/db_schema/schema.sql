CREATE TABLE IF NOT EXISTS currency (
  accountNumber INT PRIMARY KEY,
  name VARCHAR(30),
  abbr VARCHAR(3)
);

CREATE TABLE IF NOT EXISTS bank_account (
  accountNumber IDENTITY,
  owner_name VARCHAR(256) NOT NULL,
  balance DECIMAL(19,4) NOT NULL,
  blocked_amount DECIMAL(19,4) NOT NULL,
  currency_id INT NOT NULL,
  FOREIGN KEY(currency_id) REFERENCES currency(accountNumber)
);

CREATE TABLE IF NOT EXISTS transaction_status (
  accountNumber INT PRIMARY KEY,
  name VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS transaction (
  accountNumber IDENTITY,
  from_account_id BIGINT NOT NULL,
  to_account_id BIGINT NOT NULL,
  amount DECIMAL(19,4) NOT NULL,
  currency_id INT NOT NULL,
  creation_date TIMESTAMP NOT NULL,
  update_date TIMESTAMP,
  status_id INT NOT NULL,
  failMessage VARCHAR(4000),

  FOREIGN KEY(from_account_id) REFERENCES bank_account(accountNumber),
  FOREIGN KEY(to_account_id) REFERENCES bank_account(accountNumber),
  FOREIGN KEY(currency_id) REFERENCES currency(accountNumber),
  FOREIGN KEY(status_id) REFERENCES transaction_status(accountNumber)
)