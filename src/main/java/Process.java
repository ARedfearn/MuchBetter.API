import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

   /*   Process.ProcessRequest
        Here the requests are processed, a method for each endpoint. Each method calls the Class User
        which returns the users details apart from Spend which is a POST and has no response. The Details passed back
        from User are stored in a two dimensional array(a list within a list) the list contains an identifier e.g. Balance
        and value of identifier. This two dimensional array is converted to JSON and passed back to the calling class.
    */

public class Process {

    public static class ProcessRequest {
        private String Token;

        public ProcessRequest() {

        }

        public ProcessRequest(String token) {
            this.Token = token;
        }

        public String Login() throws IOException, SQLException, ClassNotFoundException {
            //Declares a User without a Token, this calls a method within User to create a new User.
            User user = new User();

            //Creates a two dimensional array that contains the identifier and value.
            //User returns the new users Token, Initial Balance, Currency and some initial transactions
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Token", user.Token)));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Balance", Integer.toString(user.Balance))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", user.Currency)));

            //Transactions form their own two dimensional array because their can be multiple transactions
            //and a transaction is made up off a date, description, amount and currency.
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Transactions")));
            twoDArrayList.addAll(user.Transactions);

            //The array is converted to JSON and passed to the calling class.
            return ConvertToJSON(twoDArrayList);
        }

        public String Balance() throws IOException, SQLException, ClassNotFoundException {
            //Declares a User with a Token, this GetDetails in User which returns the details for this Token.
            User user = new User(Token);

            //Creates a two dimensional array that contains the identifier and value.
            //For balance we only require balance and currency
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Balance", Integer.toString(user.Balance))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", user.Currency)));

            //The array is converted to JSON and passed to the calling class.
            return ConvertToJSON(twoDArrayList);
        }

        public String Transaction() throws IOException, SQLException, ClassNotFoundException {
            //The User is declared with a token so we can retrieve their transactions.
            User user = new User(Token);

            //Declaring a two dimensional array to contain the transactions.
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Transactions")));

            //Their can be multiple transactions so the Transaction field in User is of the Type TwoDimensionalArray(
            //A list within a list).
            twoDArrayList.addAll(user.Transactions);

            //The array is converted to JSON and passed to the calling class.
            return ConvertToJSON(twoDArrayList);
        }

        public void Spend(Date date, String description, int amount, String currency) throws SQLException, ClassNotFoundException {
            //Spend method returns a bool, true if it the transaction details passed are added successfully to the database.
            User user = new User(Token);
            user.spend(date, description, amount, currency);
        }

        private String ConvertToJSON(ArrayList<ArrayList<String>> list) throws IOException {
            //Here the ArrayList is converted into JSON format and returned as a String.

            //final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            //mapper.writeValue(out, list);

            //final byte[] data = out.toByteArray();
            return mapper.writeValueAsString(list);
        }
    }
}
