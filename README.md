# MuchBetter Technical Assignment

The task is to create a service that represents a simplified version of the MuchBetter e-wallet app!

We expect the assignment to take around a week to complete. Feel free to use any addiotional depedencies/libraries you like. The assignment should include unit and integration tests, not a full test suite but at least a few good examples. Dockerising the service is optional but encouraged.

## Technology Stack

- Language Java
- Framework [Spring Boot](https://spring.io/projects/spring-boot)
- In-Memory datastore [Redis](https://redis.io)

## API

The service needs four REST endpoints.  

### Login
Accepts no input and returns a `token`. The `token` will need to be supplied in the `Authorization` header in subsequent requests. Every call returns a new token and creates a new user, the user should be created with a preset balance in a preset currency.

``` shell
POST /login
```    

### Spend
Accepts an `Authorization` header and a JSON body representing a single transaction.  

``` shell
POST /spend
```

``` json
{
    "date": "2022-01-01",
    "description": "Coffee",
    "amount": 10,
    "currency": "EUR"
}
```  

### Balance

Accepts an `Authorization` and returns the current balance along with the currency code.  

``` shell
GET /balance
```

``` json
{
    "amount": 100,
    "currency": "EUR"
}
```  

### Transactions

Accepts an `Authorization` header and returns a list of transactions.  

``` shell
GET /transactions
```

``` json
[
    {
        "date": "2022-01-01",
        "description": "Coffee",
        "amount": 10,
        "currency": "EUR"
    }
]
```

## Submission

Please upload your finished assignment to either GitHub or GitLab and share the repository link with us via email. Don't squash your commits, it's nice to see how the project develops. Have fun!
