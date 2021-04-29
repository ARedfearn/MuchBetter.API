package com.wallet.module;

import com.google.inject.AbstractModule;

import com.wallet.action.WalletAction;
import com.wallet.error.WalletErrorHandler;
import com.wallet.handler.*;
import com.wallet.repository.WalletRepository;

public class WalletModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(WalletAction.class);
    bind(LoginHandler.class);
    bind(BalanceHandler.class);
    bind(SpendHandler.class);
    bind(TransactionHandler.class);
    bind(WalletRepository.class);
    bind(AuthorizationHandler.class);
    bind(WalletErrorHandler.class);
  }

}
