INSERT INTO currency (accountNumber, name, abbr)
VALUES
  (1, 'US Dollar','USD'),
  (2, 'Euro', 'EUR'),
  (3, 'Indian Rupee', 'INR');

INSERT INTO transaction_status (accountNumber, name)
VALUES
       (1, 'Planned'),
       (2, 'Processing'),
       (3, 'Failed'),
       (4, 'Succeed');

INSERT INTO bank_account (owner_name, balance, blocked_amount, currency_id)
VALUES
  ('Dharmendra Verma', 100000, 0, 3),
  ('Michael Kwakye', 1000.5, 0, 1),
  ('Test User', 1000.5, 0, 2);