package com.wallet.action;

import com.wallet.handler.*;

import ratpack.func.Action;
import ratpack.handling.Chain;

public class WalletAction implements Action<Chain> {

  @Override
  public void execute(Chain walletChain) throws Exception {

    walletChain
      .prefix("login", chain -> chain
        .post(LoginHandler.class)
      )

      .prefix("balance", chain -> chain
        .all(AuthorizationHandler.class)
        .get(BalanceHandler.class)
      )

      .prefix("spend", chain -> chain
        .all(AuthorizationHandler.class)
        .post(SpendHandler.class)
      )

      .prefix("transactions", chain -> chain
        .all(AuthorizationHandler.class)
        .get(TransactionHandler.class)
      )

      .all(ctx -> ctx.getResponse().status(401).send());
  }
}
