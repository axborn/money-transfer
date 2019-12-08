# money-transfer

Money transfer between accounts exercise implementation.

Technologies used:
spark-java, h2, hikari and good old Java.

Things that would improve the solution: CI/CD or at least deployment to container, better concurrency and parallelism, adding customer name to Account entity, endless security solutions (SSL, authentication, separation of actors and rights...), database optimization, batch clearing, financial freedom... 


# Build
gradle clean build

# Execute
java -jar build/libs/money-transfer.jar 

# API

## GET /accounts

Returns a list of all accounts in database

GDPR violation included with each request /s

200 response example: 

{"id":1,"status":"PENDING","email":"drakoniukas@email.lt","phone":"+3706666666","iban":"LT123","currency":"EUR","balance":-400},{"id":2,"status":"APPROVED","email":"kirmelyte@email.lt","phone":"86868686868","iban":"LT234","currency":"EUR","balance":600},{"id":3,"status":"CREATED","email":"oziukas@email.lt","phone":"4242424242","iban":"LT345","currency":"EUR","balance":-200},{"id":4,"status":"SUSPENDED","email":"asilas@email.lt","phone":"6969696969","iban":"LT456","currency":"EUR","balance":0}

404 response if no accounts are found

## GET /account/{accountNumber}

Returns data about requested account

200 response example{"id":1,"status":"PENDING","email":"drakoniukas@email.lt","phone":"+3706666666","iban":"LT123","currency":"EUR","balance":-400}

404 response if no accounts are found

422 response if accountNumber is not parsable as a number

## POST /account

Creates a new account. Validates that all mandatory data is in place. Returns new account with auto incremented account number.

Request body example: {"id":0,"email":"email@gmail.com","phone":"1234567890","currency":"EUR"}

201 response body example: {"id":5,"email":"email@gmail.com","phone":"1234567890","currency":"EUR"}

400 response if account data does not pass validation. Example body: Empty currency; Empty email; Empty phone; 

## GET /transactions/{accountNumber}

Returns a list of all transactions associated with an account

200 response example: [{"id":1,"fromAccount":2,"toAccount":1,"currency":"EUR","amount":100,"status":"PENDING"},{"id":2,"fromAccount":1,"toAccount":2,"currency":"EUR","amount":500,"status":"CLEARED"}]

422 response if accountNumber is not parsable as a number

## POST /transaction

Inserts a new transaction. If transaction status is PENDING then money transfer will be initiated after which transaction status will be changed to CLEARED.

Request example: {"id":0,"fromAccount":1,"toAccount":2,"currency":"EUR","amount":10,"status":"CLEARED"}

201 response example: {"id":4,"fromAccount":1,"toAccount":2,"currency":"EUR","amount":10,"status":"CLEARED"}]

400 response if account data does not pass validation. Example body: Empty origin account; Empty destination account; Empty currency; Empty amount; Empty status; 
