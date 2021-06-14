package com.wallet.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wallet.model.User;
import com.wallet.repository.WalletRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

@Singleton
public class TransactionHandler implements Handler {

  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHandler.class);

  private final WalletRepository walletRepository;

  @Inject
  public TransactionHandler(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public void handle(Context ctx) {
    LOGGER.debug("Loading transactions...");

    Promise.value(ctx.get(User.class))
      .blockingMap(user -> walletRepository.getTransactions(user.getToken()))
      .then(transactions -> ctx
        .getResponse()
        .status(200)
        .send(transactions.toString())
      );
  }

}
