package handler;

import com.google.inject.Singleton;

import error.WalletException;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.Optional;

@Singleton
public class AuthorizationHeader implements Handler {

  @Override
  public void handle(Context ctx) {

    Promise.value(ctx.getRequest().getHeaders())
      .map(headers -> Optional.ofNullable(headers.get("Authorization"))
        .orElseThrow(() -> new WalletException("Unauthorized", 401))
      )
      .then(s -> ctx.getResponse().send("Balance: 100"));
  }
}
