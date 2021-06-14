import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.Main;
import com.wallet.model.Transaction;
import com.wallet.model.User;

import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.MainClassApplicationUnderTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class WalletTest {

  MainClassApplicationUnderTest appUnderTest = new MainClassApplicationUnderTest(Main.class);

  private static final String LOGIN_ENDPOINT = "/login";
  private static final String BALANCE_ENDPOINT = "/balance";
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String INVALID_AUTHORIZATION_HEADER_VALUE = "invalidToken";

  private String login() {
    ReceivedResponse response = appUnderTest.getHttpClient().post(LOGIN_ENDPOINT);
    return response.getBody().getText();
  }

  private void createTransaction(String token, String description, int amount) {
    LocalDateTime now = LocalDateTime.now();

    ReceivedResponse response = appUnderTest
      .getHttpClient()
      .request("/spend", requestSpec ->
        requestSpec.post()
          .headers(mutableHeaders ->
            mutableHeaders
              .set(AUTHORIZATION_HEADER, token)
              .set("Content-Type", "application/json")
          )
          .body(body -> body.text(String.format("{\"date\":\"%s\",\"description\":\"%s\",\"amount\":%s,\"currency\":\"%s\"}", now.toString(), description, Integer.valueOf(amount).toString(), "GBP")))
      );

    assertThat("Response is not 2XX", response.getStatus().is2xx(), is(true));
  }

  private int getBalance(String token) throws IOException {

    ReceivedResponse response = appUnderTest
      .getHttpClient()
      .request(BALANCE_ENDPOINT, requestSpec ->
        requestSpec.get().headers(mutableHeaders ->
          mutableHeaders.set(AUTHORIZATION_HEADER, token)));

    assertThat("Response doesn't contain balance", response.getBody().getText().contains("balance"), is(true));

    ObjectMapper objectMapper = new ObjectMapper();
    User user = objectMapper.readValue(response.getBody().getText(), User.class);

    return user.getBalance();
  }

  @Test
  public void loginTest() {
    String responseText = login();

    assertThat("Response is empty", responseText.isEmpty(), is(false));
  }

  @Test
  public void shouldReturnUnauthorizedWithNoAuthorizationHeader() {
    ReceivedResponse response = appUnderTest.getHttpClient().post(BALANCE_ENDPOINT);

    assertThat("Response isn't unauthorized", response.getStatusCode(), is(401));
  }

  @Test
  public void shouldReturnUnauthorizedWithWithInvalidAuthorizationHeader() {
    ReceivedResponse response = appUnderTest
      .getHttpClient()
      .request(BALANCE_ENDPOINT, requestSpec ->
        requestSpec.get().headers(mutableHeaders ->
          mutableHeaders.set(AUTHORIZATION_HEADER, INVALID_AUTHORIZATION_HEADER_VALUE)));

    assertThat("Response isn't unauthorized", response.getStatusCode(), is(401));
  }

  @Test
  public void shouldReturnBalance() throws IOException {
    String token = login();

    assertThat("Balance should be greater than 0", getBalance(token) > 0, is(true));
  }

  @Test
  public void shouldCreateTransaction() throws IOException {
    String token = login();

    int startBalance = getBalance(token);

    createTransaction(token, "coffee", 3);

    int endBalance = getBalance(token);

    assertThat("Balance has not been reduced", endBalance < startBalance, is(true));
  }

  @Test
  public void shouldReturnTransactions() throws IOException {
    String token = login();

    createTransaction(token, "pizza", 12);

    ReceivedResponse response = appUnderTest
      .getHttpClient()
      .request("/transactions", requestSpec ->
        requestSpec.get()
          .headers(mutableHeaders ->
            mutableHeaders
              .set(AUTHORIZATION_HEADER, token)
          )
      );

    assertThat("Response is not 2XX", response.getStatus().is2xx(), is(true));

    ObjectMapper objectMapper = new ObjectMapper();
    List<Transaction> transactions = objectMapper.readValue(response.getBody().getText(), new TypeReference<List<Transaction>>() {});

    assertThat("Less thank one transaction", transactions.size() > 0, is(true));

  }
}
