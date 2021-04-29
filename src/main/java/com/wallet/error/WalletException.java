package com.wallet.error;

public class WalletException extends RuntimeException {

  private final String message;
  private final int status;

  public WalletException(String message, int status) {
    this.message = message;
    this.status = status;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }
}
