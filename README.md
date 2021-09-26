Create new third party

Post /newthirdparty with Json body similar to the below. Must have non blank name and hashKey

{
"name": "TestThird",
"hashKey": "H4shK3Y"
}

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

Post /transferfunds/{accountId}

{

"destinationAccount": 3,
"holderName": "Jeff Jefferson",
"transferAmount": 100

}

Patch /updatebalance/{accountId}

{

"balance": 500

}

Post /sendfunds (must provide valid hashkey in header, KEY hashkey, VALUE T3stTh4rd

{

"amount": 300,
"accountId": 1,
"secretKey": "SomeSecretkey"

}