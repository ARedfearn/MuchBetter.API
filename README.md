### MuchBetter Technical Assignment

The task is to create a service that represents a simplified version of the MuchBetter e-wallet app!

We expect the assignment to take around a week to complete. Feel free to use any addiotional depedencies/libraries you like. The assignemtn should include unit and integration tests, not a full test suite but a few good examples. Dockerising the service is optional but encouraged.

## API

The service needs four REST endpoints.

``` shell
POST /login
```
Accepts no input and returns a `token`. The `token` will need to be supplied in the `Authorization` header in subsequent requests. Every call returns a new token and creates a new user, the user should be created with a preset balance in a preset currency.

``` shell
GET /balance
```
Accepts an `Authorization` with the token created at login, returns the current balance along with the currency code.


``` shell
GET /transactions
```
Accepts an `Authorization` header, with the token created at login, and returns a list of transactions in the format:


``` json
{
    "date": "2022-01-01",
    "description": "Coffee",
    "amount": 10,
    "currency": "EUR"
}
```

``` shell
POST /spend
```
Accepts an `Authorization` header, with the token created at `/login`, and a JSON body representing one transaction in the format detailed above.


### Technology Stack

- Java
- Spring
- [Redis](https://redis.io) as the in-memory datastore.
