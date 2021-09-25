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