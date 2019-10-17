import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.util.MultiValueMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

   /*
        Here the requests are processed, a method for each endpoint. Each method calls the Class User
        which returns the users details apart from Spend which is a POST and has no response. The Details passed back
        from User are stored in a two dimensional array(a list within a list) the list contains an identifier e.g. Balance
        and value of identifier. This two dimensional array is converted to JSON and passed back to the calling class.
    */


public class Process {
    private String Token;
    public int Status = 200;

    public Process() {

    }

    public Process(String token) {

        this.Token = token;
    }

    public String Login() {
        //Declares a User without a token.
        User user = new User();
        try
        {
            //As no token is passed the GetDetails and GetTransactions methods will create a new user.
            //Returning their token, initial balance, currency and transactions.
            user.GetDetails();
            user.GetTransaction();
        }
        catch (SQLException e) {
            Status = 400;
            return "[{Error : " + e.getMessage() + "}]";
        }

        //The classes properties are converted to JSON and passed back to the calling class as a string.
        return ConvertToJSON(user);
    }

    public String Balance() {
        if(Token == null){
            Status = 400;
            return "Invalid Token";
        }
        //Declares a User with a Token, then calls a method to retrieve the Users details, identified by the token.
        User user = new User(Token);
        try
        {
            user.GetDetails();
            //Token is set to null so it will not be returned in the JSON. As this is a call for the balance, we only
            //want the Balance and Currency. The Users transactions are not returned as this property is null because
            //GetTransactions is not called.
            user.Token = null;
        }
        catch (SQLException e)
        {
            Status = 400;
            return "[{Error : " + e.getMessage() + "}]";
        }

        return ConvertToJSON(user);
    }

    public String Transaction() {
        if(Token == null) {
            Status = 400;
            return "Invalid Token";
        } else {
            User user = new User(Token);
            try
            {
                //The User class gets the transactions associated with the following token and stores each transaction in a list.
                user.GetTransaction();
                //Currency is already null.
                user.Token = null;
                user.Balance = null;
            }
            catch (SQLException e)
            {
                Status = 400;
                return "[{Error : " + e.getMessage() + "}]";
            }

            return ConvertToJSON(user);
        }
    }

    public String Spend(MultiValueMap<String, String> params) {
        if(params.isEmpty()) {
            Status = 400;
            return "No Parameters";
        } else {
            java.sql.Date sqlDate = null;
            try {
                User user = new User(Token);
                try {
                    //Converting the date passed to a SQL readable date to be passed to User and then the H2 Database
                    java.util.Date utilDate = new SimpleDateFormat("dd-MM-yyyy").parse(params.get("Date"));
                    sqlDate = new java.sql.Date(utilDate.getTime());
                } catch (ParseException e) {
                    Status = 400;
                    return "[{Error : " + e.getMessage() + "}]";
                }
                //The parameters are passed to user which adds a transaction based upon these values and updates the users balance.
                user.spend(sqlDate, params.get("Description"), Integer.parseInt(params.get("Amount")), params.get("Currency"));
            } catch (SQLException e) {
                Status = 400;
                return "[{Error : " + e.getMessage() + "}]";
            }
            Status = 201;
            return "Success";
        }
    }

    private String ConvertToJSON(User user) {
        //Here the class is converted to a JSON format. Each populated property is returned.
        final byte[] data;
        try
        {
            String output;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            //Formatting the transaction date returned within the JSON.
            DateFormat df = new SimpleDateFormat("dd-MM-yy");
            mapper.setDateFormat(df);

            //Pretty printer will properly indent the JSON.
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, user);
            data = out.toByteArray();
        }
        catch (IOException e)
        {
            Status = 400;
            return "[ {Error : " + e.getMessage() + "}]";
        }

        return (new String(data));
    }

}

