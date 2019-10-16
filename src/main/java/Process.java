import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ratpack.util.MultiValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

   /*
        Here the requests are processed, a method for each endpoint. Each method calls the Class User
        which returns the users details apart from Spend which is a POST and has no response. The Details passed back
        from User are stored in a two dimensional array(a list within a list) the list contains an identifier e.g. Balance
        and value of identifier. This two dimensional array is converted to JSON and passed back to the calling class.
    */

public class Process {

    public static class ProcessRequest {
        private String Token;
        public int Status = 200;

        public ProcessRequest() {

        }

        public ProcessRequest(String token) {
            this.Token = token;
        }

        public String Login() throws IOException {
            //Declares a new User without a token then calls a method to create a new user and populate User with their details.
            User user = new User();
            //The methods within user all return a boolean if false it means an exception has been thrown and caught.
            if(!user.GetDetails()){
                Status = 400;
                return user.ErrorMessage;
            }

            //Upon successfully creating the new user and populating the class with said users details.
            //A two dimensional array that contains the name of the detail and it's value is created, to hold these details.
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Token", user.Token)));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Balance", Integer.toString(user.Balance))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", user.Currency)));

            //Transactions form their own two dimensional array because their can be multiple
            //A transaction is made up off a date, description, amount and currency.
            twoDArrayList.addAll(user.Transactions);

            //The array is converted to JSON and passed back to the calling class.
            return ConvertToJSON(twoDArrayList);
        }

        public String Balance() throws IOException {
            //Declares a User with a Token, then calls a method to retrieve the Users details, identified by the token and populate User with the.
            User user = new User(Token);
            if(!user.GetDetails()){
                Status = 400;
                return user.ErrorMessage;
            }

            //For a balance request we only require balance and currency to be added to the two dimensional array
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Balance", Integer.toString(user.Balance))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", user.Currency)));

            return ConvertToJSON(twoDArrayList);
        }

        public String Transaction() throws IOException {
            User user = new User(Token);
            if(!user.GetDetails()){
                Status = 400;
                return user.ErrorMessage;
            }

            //Transactions are already an array so we convert them
            return ConvertToJSON(user.Transactions);
        }

        public String Spend(MultiValueMap<String, String> params) throws ParseException {
            User user = new User(Token);
            //Converting the date passed to a SQL readable date to be passed to User and then the H2 Database
            java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(params.get("Date"));
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

            //The parameters are passed to user which adds a transaction based upon these values and updates the users balance
            if(!user.spend(sqlDate, params.get("Description"), Integer.parseInt(params.get("Amount")), params.get("Currency"))) {
                Status = 400;
                return user.ErrorMessage;
            }

            return "Success";
        }

        private String ConvertToJSON(ArrayList<ArrayList<String>> list) throws IOException {
            //Here the ArrayList is converted into JSON format and returned as a String.
            String output;

            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            output = mapper.writeValueAsString(list);

            return output;
        }
    }
}
