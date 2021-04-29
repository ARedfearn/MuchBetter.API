package com.wallet.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.wallet.error.WalletException;
import com.wallet.model.User;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.registry.Registry;
import com.wallet.repository.WalletRepository;

import java.util.Objects;
import java.util.Optional;

@Singleton
public class AuthorizationHandler implements Handler {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  private final WalletRepository walletRepository;

  @Inject
  public AuthorizationHandler(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  @Override
  public void handle(Context ctx) {

    Promise.value(ctx.getRequest().getHeaders())
      .map(headers -> Optional.ofNullable(headers.get(AUTHORIZATION_HEADER))
        .orElseThrow(() -> new WalletException("Unauthorized", 401))
      )
      .blockingMap(walletRepository::getUser)
      .mapIf(Objects::isNull,
        user -> { throw new WalletException("Unauthorized", 401);}
      )
      .then(user -> ctx.next(Registry.of(r -> r.add(User.class, user))));
  }
}
