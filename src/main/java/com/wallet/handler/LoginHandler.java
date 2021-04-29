package com.wallet.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.wallet.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import com.wallet.repository.WalletRepository;
import com.wallet.util.TokenGenerator;

@Singleton
public class LoginHandler implements Handler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginHandler.class);

  private static final String DEFAULT_CURRENCY = "EUR";
  private static final int DEFAULT_BALANCE = 100;

  private final WalletRepository walletRepository;

  @Inject
  public LoginHandler(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public void handle(Context ctx) {
    LOGGER.debug("Logging in...");

    Promise.value(ctx)
      .map(context -> createUser())
      .blockingMap(walletRepository::setUser)
      .then(user -> ctx.render(user.getToken()));
  }

  private User createUser() {
    return new User(TokenGenerator.generate())
      .setCurrency(DEFAULT_CURRENCY)
      .setBalance(DEFAULT_BALANCE);
  }
}
