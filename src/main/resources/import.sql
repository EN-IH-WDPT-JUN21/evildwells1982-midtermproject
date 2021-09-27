INSERT INTO users (name, username, password, roles) VALUES ('Admin', 'Admin123', '$2a$15$UZXJ7Y.xkXsAYix9P9EZHeTL90JjevQ5XPaxxwc6HskCNelhwxg4C', 'Admin');
INSERT INTO admin (user_id) Values (1);

INSERT INTO address (postcode, street) VALUES ('LE191AA', '39 Street Street');
INSERT INTO users (name, username, password, roles) VALUES ('Jeff Jefferson', 'JeffJefferson123','$2a$15$4L3NghR8BxEHk9Q3Vnf.meuUyCEzqip5BpZGbNibRA9cGXSNixtNy', 'Account_Holder');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, holder_id) VALUES ('2001-02-11', null, 1, 2);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 500.00, 'USD', '2018-09-18',40,'USD',2,null);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, account_id) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 1);

INSERT INTO address (postcode, street) VALUES ('LE204GH', '40 Avenue Street');
INSERT INTO users (name, username, password, roles) VALUES ('Pete Peterson', 'PetePeterson123', '$2a$15$MxEv1zeJortd5mIYNHs5j.XHSB0YyNQTWAy1tgiNYR2lR7fK0Nsvu', 'Account_Holder');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, holder_id) VALUES ('2020-02-11', null, 2, 3);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 1500.00, 'USD', '2020-06-10',40,'USD',3,null);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, account_id) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 2);

INSERT INTO address (postcode, street) VALUES ('YO126JJ', '90 Road Street');
INSERT INTO address (postcode, street) VALUES ('JJ985FG', '55 Road Lane');
INSERT INTO users (name, username, password, roles) VALUES ('Molly Mollson', 'MollyMollson123', '$2a$15$5JAJs0umLh8EBqFp6XOrlOEEplAj3Ag3no80UQJdGH3Yop5QXJBeG', 'Account_Holder');
INSERT INTO account_holder (date_of_birth, mailing_address_id, primary_address_id, holder_id) VALUES ('1985-06-24', 4, 3, 4);
INSERT INTO account (account_status, balance_amount, balance_currency, creation_date, penalty_amount, penalty_currency, primary_id, secondary_id) VALUES ('ACTIVE', 5500.00, 'USD', '2010-09-18',40,'USD',4,2);
INSERT INTO checking_account (minimum_balance_amount, minimum_balance_currency, maintenance_amount, maintenance_currency, secret_key, account_id) values (250.00, 'USD', 12.00, 'USD', 'SomeSecretkey', 3);

INSERT INTO users (name, username, password, roles) VALUES ('Test Third Party 1', 'TestThirdParty123', '$2a$15$jxcz0qF3sVJV5Btj2h.EXuOOFowBaSO6IUbINmejNUqoM0cKvzwHG', 'Third_Party');
INSERT INTO third_party (hash_key, user_id) VALUES ('T3stTh4rd',5);




