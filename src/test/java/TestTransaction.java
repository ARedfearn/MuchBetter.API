import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ratpack.http.client.ReceivedResponse;
import ratpack.test.embed.EmbeddedApp;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestTransaction {

    @Test
    public void testGetTransaction() throws Exception {
        EmbeddedApp.fromHandlers(chain -> chain
                .path("login",new LoginHandler())
                .path("transaction",new TransactionHandler())
        ).test(httpClient -> {
            //Login and get token
            String loginResponse = httpClient.get("login").getBody().getText();

            ObjectMapper objectMapper = new ObjectMapper();
            User login = objectMapper.readValue(loginResponse, User.class);

            assertNotNull(login.Token);

            //Get the transactions for the provided token
            ReceivedResponse transactionResponse = httpClient
                    .requestSpec(requestSpec ->
                            requestSpec.getHeaders().set("Authorization", login.Token)
                    ).get("transaction");

            String jsonTransaction = transactionResponse.getBody().getText();
            User user = objectMapper.readValue(jsonTransaction, User.class);

            Date date = new Date();
            java.sql.Date today = new java.sql.Date(date.getTime());
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

            Transaction transaction = user.Transactions.get(0);

            //Check if the transactions match the initial transactions provided to a user
            assertEquals(formatter.format(today), formatter.format(transaction.Date));
            assertEquals("Initial Transaction", transaction.Description);
            assertEquals(100, transaction.Amount.intValue());
            assertEquals("GBP", transaction.Currency);

            //Clean up database
            CleanDatabase clean = new CleanDatabase(login.Token);
            clean.DeleteTransaction();
            clean.DeleteUser();
        });
    }
}
