package com.wallet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.wallet.model.User;

import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

@Singleton
public class BalanceHandler implements Handler {

  public void handle(Context ctx) {

    Promise.value(ctx.get(User.class))
      .then(user -> ctx
        .getResponse()
        .status(200)
        .send(new ObjectMapper().writeValueAsString(user))
      );
  }
}
