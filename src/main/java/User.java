import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class User {

    public String Token;
    public String Currency;
    public int Balance;
    public ArrayList<ArrayList<String>> Transactions;

    //Relays any error messages (SQL Exceptions) From the DataLayer back to the processing layer.
    public String ErrorMessage;

    public User() {

    }

    public User(String token) {
        Token = token;
    }

    public boolean GetDetails() {
        //If no token is passed to the DataLayer a new user is created and it's details are returned.
        DataLayer dl = new DataLayer();
        //The users details are returned from the data layer as a result set.
        ResultSet rs = dl.GetUser(this);

        //The result set is iterated through and each detail is written to the appropriate User field
        try {
            while (rs.next()) {
                Token = rs.getString(2);
                Balance = rs.getInt(3);
                Currency = rs.getString(4);
            }
        }
        catch (SQLException e){
            ErrorMessage = e.getMessage();
            return false;
        }

        return !dl.Error && GetTransaction();
    }

    private boolean GetTransaction() {
        try {
            ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();
            //The transaction collection Class is Iterable and is populated with each transaction a User owns.
            TransactionCollection tc = new TransactionCollection(Token);
            //The foreach loop iterates through each Transaction in the TransactionCollection for a specified Token.
            //Each transactions details are added to a two dimensional array which is assigned to the field Transactions.
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            for (Transaction tran : tc) {
                twoDArrayList.add(new ArrayList<String>(Arrays.asList("Date", df.format(tran.Date))));
                twoDArrayList.add(new ArrayList<String>(Arrays.asList("Description", tran.Description)));
                twoDArrayList.add(new ArrayList<String>(Arrays.asList("Amount", Integer.toString(tran.Amount))));
                twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", tran.Currency)));
            }
            Transactions = twoDArrayList;
        }
        catch (SQLException e){
            ErrorMessage = e.getMessage();
            return false;
        }

        return true;
    }

    public boolean spend(Date date, String description, int amount, String currency) {
        //The spend method adds a transaction for a specific Token. Returning a bool representing weather it was added
        //Successfully or not.
        DataLayer dl = new DataLayer();
        dl.AddTransaction(Token, date, description, amount, currency);

        return !dl.Error;
    }
}
