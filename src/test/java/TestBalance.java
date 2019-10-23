import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestBalance {
    @Test
    public void testGetBalance() throws Exception {
        EmbeddedApp.fromHandlers(chain -> chain
                .path("login",new LoginHandler())
                .path("balance",new BalanceHandler())
        ).test(httpClient -> {
            //Login and get token
            String loginResponse = httpClient.get("login").getBody().getText();

            ObjectMapper objectMapper = new ObjectMapper();
            User login = objectMapper.readValue(loginResponse, User.class);

            assertNotNull(login.Token);

            //Get the balance for the provided token
            ReceivedResponse balanceResponse = httpClient
                    .requestSpec(requestSpec ->
                            requestSpec.getHeaders().set("Authorization", login.Token)
                    ).get("balance");

            String jsonBalance = balanceResponse.getBody().getText();
            User user = objectMapper.readValue(jsonBalance, User.class);

            //Check if the balance and currency are equal to the balance and currency initially given to all users.
            Integer balance = 100;
            assertEquals(balance, user.Balance);
            assertEquals("GBP", user.Currency);

            //Clean up database
            CleanDatabase tdl = new CleanDatabase(login.Token);
            tdl.DeleteTransaction();
            tdl.DeleteUser();
        });
    }
}
