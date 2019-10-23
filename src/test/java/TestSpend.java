import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestSpend {
    private static final String Json =
            "{" +
                    "\"Date\":\"2019-10-22\"," +
                    "\"Description\":\"TestTransaction\"," +
                    "\"Amount\":\"1000\"," +
                    "\"Currency\":\"GBP\"" +
                    "}";

    @Test
    public void testPostSpend() throws Exception {
        EmbeddedApp.fromHandlers(chain -> chain
                .path("login", new LoginHandler())
                .path("spend",new SpendHandler())
                .path("transaction", new TransactionHandler())
                .path("balance", new BalanceHandler())
        ).test(httpClient -> {
            int balance = 0;

            //Login and get token
            String loginResponse = httpClient.get("login").getBody().getText();

            ObjectMapper objectMapper = new ObjectMapper();
            User login = objectMapper.readValue(loginResponse, User.class);

            assertNotNull(login.Token);
            balance = login.Balance;

            //Add a transaction to the token
            ReceivedResponse spendResponse = httpClient
                    .requestSpec(requestSpec -> {
                                requestSpec.getHeaders().set("Authorization", login.Token)
                                        .set("Content-Type", "application/json");
                                requestSpec.getBody().text(Json);
                            }
                    ).get("spend");

            assertEquals(201, spendResponse.getStatusCode());

            //Get the balance
            ReceivedResponse balanceResponse = httpClient
                    .requestSpec(requestSpec ->
                            requestSpec.getHeaders().set("Authorization", login.Token)
                    ).get("balance");

            String jsonBalance = balanceResponse.getBody().getText();
            User balanceUser = objectMapper.readValue(jsonBalance, User.class);

            //Check if the balance has been updated
            int expectedBalance = balance + 1000;
            assertEquals(expectedBalance, balanceUser.Balance.intValue());

            //Clean up DataBase
            CleanDatabase tdl = new CleanDatabase(login.Token);
            tdl.DeleteTransaction();
            tdl.DeleteUser();

        });
    }
}
