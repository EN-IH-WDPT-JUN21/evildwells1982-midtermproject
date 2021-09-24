INSERT INTO users (name) VALUES ('System');
INSERT INTO admin (user_id) Values (1);

INSERT INTO address (postcode, street) VALUES ('LE191AA', '39 Street Street');
INSERT INTO users (name) VALUES ('Jeff Jefferson');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, holder_id) VALUES ('2001-02-11', null, 1, 2);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 500.00, 'USD', '2018-09-18',40,'USD',2,null);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, account_id) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 1);

INSERT INTO address (postcode, street) VALUES ('LE204GH', '40 Avenue Street');
INSERT INTO users (name) VALUES ('Pete Peterson');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, holder_id) VALUES ('1960-02-11', null, 2, 3);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 1500.00, 'USD', '2020-06-10',40,'USD',3,null);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, account_id) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 2);

INSERT INTO address (postcode, street) VALUES ('YO126JJ', '90 Road Street');
INSERT INTO address (postcode, street) VALUES ('JJ985FG', '55 Road Lane');
INSERT INTO users (name) VALUES ('Molly Mollson');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, holder_id) VALUES ('1985-06-24', 4, 3, 4);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 5500.00, 'USD', '2010-09-18',40,'USD',4,2);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, account_id) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 3);

INSERT INTO users (name) VALUES ('TestThirdParty1');
INSERT INTO third_party (hash_key, user_id) VALUES ('T3stTh4rd',5);




