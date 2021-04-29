package com.wallet.handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.wallet.error.WalletException;
import com.wallet.model.Transaction;
import com.wallet.model.User;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.Jackson;
import com.wallet.repository.WalletRepository;

@Singleton
public class SpendHandler implements Handler {

  private final WalletRepository walletRepository;

  @Inject
  public SpendHandler(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public void handle(Context ctx) {

    Promise.value(ctx)
      .flatRight(context -> context.parse(Jackson.fromJson(Transaction.class)))
      .map(pair -> pair.left.get(User.class).setTransaction(pair.right))
      .mapIf(user -> user.getBalance() < user.getTransaction().getAmount(),
        user -> {throw new WalletException("Insufficient funds", 400);},
        user -> user.setBalance(user.getBalance() - user.getTransaction().getAmount())
      )
      .blockingMap(walletRepository::setUser)
      .blockingMap(user -> walletRepository.setTransaction(user.getToken(), user.getTransaction()))
      .then(pair -> ctx.getResponse().send());
  }
}
