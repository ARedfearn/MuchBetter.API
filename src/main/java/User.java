import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class User {

    public String Token;
    public String Currency;
    public int Balance;
    public ArrayList<ArrayList<String>> Transactions;

    public User() {
        //Return a Token if no token is passed to the user class
        //Create a new user and return the new token then get details
        Token = "ABCD";
        GetDetails();
    }

    public User(String token) {
        //This will populate the User with a token.
        Token = token;
        GetDetails();
    }

    private void GetDetails(){
        Balance = 100;

        Currency = "GBP";

        GetTransaction();
    }

    private void GetTransaction() {
        //Call to transaction class with token to get a list of transactions
        //Or without to get some initial transactions
        ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
        twoDArrayList.add(new ArrayList<String>(Arrays.asList("Date", "12/10/2019")));
        twoDArrayList.add(new ArrayList<String>(Arrays.asList("Description", "Transfer to Alex")));
        twoDArrayList.add(new ArrayList<String>(Arrays.asList("Amount", "50")));
        twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", "GBP")));

        Transactions = twoDArrayList;
    }

    public void spend(Date date, String description, int amount, String currency) {
        //Call to database to spend, adding date, description, amount and currency
        //Update new balance
    }
}
