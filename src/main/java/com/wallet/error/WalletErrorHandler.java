package com.wallet.error;

import com.google.inject.Singleton;

import ratpack.error.ServerErrorHandler;
import ratpack.exec.Promise;
import ratpack.handling.Context;

@Singleton
public class WalletErrorHandler implements ServerErrorHandler {

  @Override
  public void error(Context context, Throwable throwable) {

    Promise.value(throwable)
      .mapIf(exception -> exception instanceof WalletException,
        exception -> (WalletException) exception,
        exception -> new WalletException("Oops! Something went wrong", 500)
      )
      .then(e -> context.getResponse().status(e.getStatus()).send(e.getMessage()));
  }
}
