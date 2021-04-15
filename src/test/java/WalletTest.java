import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.MainClassApplicationUnderTest;

public class WalletTest {

  MainClassApplicationUnderTest appUnderTest = new MainClassApplicationUnderTest(Main.class);

  private static String LOGIN_ENDPOINT = "/login";
  private static String BALANCE_ENDPOINT = "/balance";
  private static String AUTHORIZATION_HEADER = "Authorization";
  private static String INVALID_AUTHORIZATION_HEADER_VALUE = "invalidToken";

  private String login() {
    ReceivedResponse response = appUnderTest.getHttpClient().get(LOGIN_ENDPOINT);
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
}
