import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.MainClassApplicationUnderTest;

public class WalletTest {

  MainClassApplicationUnderTest appUnderTest = new MainClassApplicationUnderTest(Main.class);

  @Test
  public void loginTest() {
    ReceivedResponse response = appUnderTest.getHttpClient().get("/login");
    String responseText = response.getBody().getText();

    assertThat("Response is empty", responseText.isEmpty(), is(false));
  }

  @Test
  public void shouldReturnUnauthorized() {
    ReceivedResponse response = appUnderTest.getHttpClient().post("/balance");
    int responseStatus = response.getStatusCode();

    assertThat("Response isn't unauthorized", responseStatus, is(401));
  }
}
