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
"secondaryId": 4

}