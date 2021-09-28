**Mid Term Project**

**Application Properties (for preparation of MySQL database and user)**

spring.datasource.url=jdbc:mysql://localhost:3306/midtermproject
spring.datasource.username=ironhacker
spring.datasource.password=1r0nH@ck3r

**Test Data populated in Database at execution**

**Authorised Users**

Admin User:
Username: Admin
Password: Admin

Third Party User:
Username: TestThirdParty123
Password: TestThirdParty1

Account Holders:
Username: JeffJefferson123
Password: JeffJefferson

Has 2 checking accounts (Account id: 1 and Account id: 3)

Username: Pete Peterson123
Password: PetePeterson


Has 1 checking account (Account id: 2)

Username: MollyMollson123
Password: MollyMollson

Has 1 checking account (Account id: 3)

**Endpoints Created: -**

**Check own Account Balances (Account_Holders only)**

Get /myaccounts


**Create new third party (Admin Only)**

Post /newthirdparty with Json body similar to the below. Must have non blank name and hashKey

{
"name": "TestThird",
"hashKey": "H4shK3Y"
}

**Create New Account (Admin Only)**

Post /newaccount/checking

{
"balance": 6000,
"secretKey": "S3cretK3Y",
"primaryId": 3,
"secondaryId": 4 (optional)

}

Post /newaccount/savings

{
"balance": 6000,
"secretKey": "S3cretK3Y",
"primaryId": 3,
"secondaryId": 4, (optional)
"interestRate": 0.3, (optional, if present then must be between 0 and 0.5 inclusive)
"minimumBalance": 200 (optional, if present then must be between 100 and 1000 inclusive)

}

post /newaccount/creditcard

{

"balance": 100, (must be equal to or lower that credit limit (default credit limit is 100 unless specified otherwise in this request))
"primaryId": 3,
"secondaryId": 4, (optional)
"creditLimit": 100, (optional)
"interestRate": 0.1 (optional)

}

**Transfer Funds (Account_Holder only)**

Post /transferfunds/{accountId}

{

"destinationAccount": 3,
"holderName": "Jeff Jefferson",
"transferAmount": 100

}

**Update Balance (Admin Only)**

Patch /updatebalance/{accountId}

{

"balance": 500

}

**Send Funds to Account (Third Party Only)**

Post /sendfunds (must provide valid hashkey in header, KEY hashkey, VALUE T3stTh4rd

{

"amount": 300,
"accountId": 1,
"secretKey": "SomeSecretkey"

}

**Take Funds from Account (Third Party Only)**

Post /claimfunds (must provide valid hashkey in header, KEY hashkey, VALUE T3stTh4rd

{

"amount": 300,
"accountId": 1,
"secretKey": "SomeSecretkey"

}
