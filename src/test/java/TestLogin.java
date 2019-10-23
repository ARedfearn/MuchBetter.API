import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ratpack.test.embed.EmbeddedApp;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestLogin {

    @Test
    public void testGetLogin() throws Exception {
        EmbeddedApp.fromHandlers(chain -> chain
                .path("login",new LoginHandler())
        ).test(HttpClient -> {

            //Attempt to login
            String loginResponse = HttpClient.get("login").getBody().getText();

            //Convert json response to the user class
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(loginResponse, User.class);

            //Check that a token is returned
            assertNotNull(user.Token);

            //Check the initial balance and currency are correct
            Integer balance = 100;
            assertEquals(balance, user.Balance);
            assertEquals("GBP", user.Currency);

            LocalDate locald = LocalDate.now();
            java.sql.Date today = Date.valueOf(locald);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

            //Check the initial transaction is correct
            Transaction transaction = user.Transactions.get(0);
            assertEquals(formatter.format(today), formatter.format(transaction.Date));
            assertEquals("Initial Transaction", transaction.Description);
            assertEquals(100, transaction.Amount.intValue());
            assertEquals("GBP", transaction.Currency);

            //Clean up database
            CleanDatabase clean = new CleanDatabase(user.Token);
            clean.DeleteTransaction();
            clean.DeleteUser();

        });
    }
}
