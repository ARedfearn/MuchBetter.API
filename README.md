### MuchBetter Technical Assignment

The task is to create a service that represents a simplified version of the MuchBetter e-wallet app!

We expect the assignemtn to take around a week to complete. Feel free to use any addiotional depedencies/libraries you like. 

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

### Requirements

- Implement the 4 API endpoints
- Feel free to spend as much or as little time on the exercise as you like
- Feel free to use whatever additional frameworks / libraries / packages you like
- Your code should be in a state that you would feel comfortable releasing to production
- Writing unit/integration tests are optional but highly encouraged
- Dockerising the two components is optional and encouraged
