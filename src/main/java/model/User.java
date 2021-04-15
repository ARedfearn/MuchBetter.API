package model;

public class User {

  private final String token;
  private String currency;
  private int balance;
  private Transaction transaction;

  public User(String token) {
    this.token = token;
  }

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
