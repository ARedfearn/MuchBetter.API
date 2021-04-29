package com.wallet.handler;

import com.google.inject.Singleton;

import com.wallet.model.User;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

@Singleton
public class BalanceHandler implements Handler {

  public void handle(Context ctx) {

    Promise.value(ctx.get(User.class))
      .then(user -> ctx.getResponse().send(String.format("{\"Currency\":\"%s\",\"Balance\":%d}", user.getCurrency(), user.getBalance())));
  }
}
