package com.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  public User(@JsonProperty("currency") String currency, @JsonProperty("balance") int balance) {
    this.currency = currency;
    this.balance = balance;
  }

  public User(String token) {
    this.token = token;
  }

  @JsonIgnore
  private String token;

  private String currency;
  private int balance;
  private Transaction transaction;

  public String getToken() {
    return token;
  }

  public String getCurrency() {
    return currency;
  }

  public User setCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public int getBalance() {
    return balance;
  }

  public User setBalance(Integer balance) {
    this.balance = balance;
    return this;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public User setTransaction(Transaction transaction) {
    this.transaction = transaction;
    return this;
  }
}
