package handler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import model.User;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import repository.WalletRepository;
import util.TokenGenerator;

@Singleton
public class LoginHandler implements Handler {

  private static final String DEFAULT_CURRENCY = "EUR";
  private static final int DEFAULT_BALANCE = 100;

  private final WalletRepository walletRepository;

  @Inject
  public LoginHandler(WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public void handle(Context ctx) {

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
