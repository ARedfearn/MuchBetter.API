package com.wallet;

import com.wallet.action.WalletAction;
import com.wallet.module.WalletModule;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;

public class Main {

  public static void main(String... args) throws Exception {
    RatpackServer.start(server -> server
      .registry(Guice.registry(r -> r
        .module(WalletModule.class)))
      .handlers(c -> {
          c.insert(WalletAction.class);
        }
      )
    );
  }
}
