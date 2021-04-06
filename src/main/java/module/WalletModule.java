package module;

import com.google.inject.AbstractModule;

import action.WalletAction;
import handler.BalanceHandler;
import handler.LoginHandler;
import handler.SpendHandler;
import handler.TransactionHandler;
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
  }

}
