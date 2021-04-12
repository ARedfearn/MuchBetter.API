package module;

import com.google.inject.AbstractModule;

import action.WalletAction;
import error.WalletErrorHandler;
import handler.*;
import repository.WalletRepository;

public class WalletModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(WalletAction.class);
    bind(LoginHandler.class);
    bind(BalanceHandler.class);
    bind(SpendHandler.class);
    bind(TransactionHandler.class);
    bind(WalletRepository.class);
    bind(AuthorizationHeader.class);
    bind(WalletErrorHandler.class);
  }

}
