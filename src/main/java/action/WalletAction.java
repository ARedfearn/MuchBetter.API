package action;

import handler.*;
import ratpack.func.Action;
import ratpack.handling.Chain;

public class WalletAction implements Action<Chain> {

  @Override
  public void execute(Chain walletChain) throws Exception {

    walletChain
      .prefix("login", chain -> chain
        .get(LoginHandler.class)
      )

      .prefix("balance", chain -> chain
        .all(AuthorizationHeader.class)
        .get(BalanceHandler.class)
      )

      .prefix("spend", chain -> chain
        .all(SpendHandler.class)
      )

      .prefix("transaction", chain -> chain
        .all(TransactionHandler.class)
      )

      .all(ctx -> ctx.getResponse().status(401).send());
  }
}
