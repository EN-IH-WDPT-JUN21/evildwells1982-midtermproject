INSERT INTO address (postcode, street) VALUES ('Test Postcode', 'Test Street');
INSERT INTO users (name) VALUES ('Test Customer');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, user_id) VALUES ('2014-02-11', null, 1, 1);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 500.00, 'USD', '2021-09-18',40,'USD',1,null);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, accountid) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 1);
