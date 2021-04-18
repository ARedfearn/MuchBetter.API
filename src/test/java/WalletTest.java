import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.MainClassApplicationUnderTest;

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
  public void shouldReturnBalance() {
    String token = login();

    ReceivedResponse response = appUnderTest
      .getHttpClient()
      .request(BALANCE_ENDPOINT, requestSpec ->
        requestSpec.get().headers(mutableHeaders ->
          mutableHeaders.set(AUTHORIZATION_HEADER, token)));

    assertThat("Response doesn't contain balance", response.getBody().getText().contains("Balance"), is(true));
  }

  @Test
  public void shouldCreateTransaction() {
    String token = login();

    ReceivedResponse response = appUnderTest
      .getHttpClient()
      .request("/spend", requestSpec ->
        requestSpec.post()
          .headers(mutableHeaders ->
            mutableHeaders
              .set(AUTHORIZATION_HEADER, token)
              .set("Content-Type", "application/json")
          )
          .body(body -> body.text("{\"date\":\"2012-04-23T18:25:43.511Z\",\"description\":\"coffee\",\"amount\":3,\"currency\":\"GBP\"}"))
      );

    assertThat("Response is not 2XX", response.getStatus().is2xx(), is(true));

    ReceivedResponse balanceResponse = appUnderTest
      .getHttpClient()
      .request("/balance", requestSpec ->
        requestSpec.get()
          .headers(mutableHeaders ->
            mutableHeaders
              .set(AUTHORIZATION_HEADER, token)
          )
      );

    assertThat("Response has not been reduced", balanceResponse.getBody().getText().contains("97"), is(true));
  }
}
