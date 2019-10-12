import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Process {

    public static class ProcessRequest {
        private String Token;

        public ProcessRequest() {

        }

        public ProcessRequest(String token) {
            this.Token = token;
        }

        public String Login() throws IOException {
            //Declares a new User without a Token as this is a new user
            User user = new User();

            //Creates a two dimensional array that contains the JSON identifier and value.
            //A new user returns a Token, Initial Balance, Currency and some initial transactions
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Token", user.Token)));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Balance", Integer.toString(user.Balance))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", user.Currency)));

            //Transactions form their own two dimensional array which is added to the array declared in the login method
            //Transactions are added under the node Transactions
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Transactions")));
            twoDArrayList.addAll(user.Transactions);

            //The array is then converted to JSON and passed to the Java.Main to be returned as a response
            return ConvertToJSON(twoDArrayList);
        }

        public String Balance() throws IOException{
            //A user is instantiated with a Token so that the balance and currency for this user can be retrieved
            User user = new User("ABCD");

            //Again a two dimensional array is declared to hold the JSON identifier and value.
            //For balance we must return the balance and currency
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Balance", Integer.toString(user.Balance))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", user.Currency)));

            return ConvertToJSON(twoDArrayList);
        }

        public String Transaction() throws IOException{
            //Again the User is declared with a token so we can retrieve their transactions
            User user = new User("ABCD");

            //Return a list of transactions in JSON format under the node transactions
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Transactions")));
            twoDArrayList.addAll(user.Transactions);

            return ConvertToJSON(twoDArrayList);
        }

        public boolean Spend() {
            return true;
        }

        private String ConvertToJSON(ArrayList<ArrayList<String>> list) throws IOException {
            //final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            //mapper.writeValue(out, list);

            //final byte[] data = out.toByteArray();
            return mapper.writeValueAsString(list);
        }
    }
}
