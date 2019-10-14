import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class User {

    public String Token;
    public String Currency;
    public int Balance;
    public ArrayList<ArrayList<String>> Transactions;

    public User() throws SQLException, ClassNotFoundException {
        GetDetails();
    }

    public User(String token) throws SQLException, ClassNotFoundException {
        Token = token;
        GetDetails();
    }

    private void GetDetails() throws SQLException, ClassNotFoundException {
        //If no token is passed to the DataLayer a new user is created and it's details are returned.
        DataLayer dl = new DataLayer();
        ResultSet rs = dl.GetUser(this);

        while(rs.next()){
            Token = rs.getString(2);
            Balance = rs.getInt(3);
            Currency = rs.getString(4);
        }

        GetTransaction();
    }

    private void GetTransaction() throws SQLException, ClassNotFoundException {
        ArrayList<ArrayList<String>> twoDArrayList = new ArrayList<ArrayList<String>>();

        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);

        //The transaction collection Class is Iterable and is populated with each transaction a User owns.
        TransactionCollection tc = new TransactionCollection(Token);
        //The foreach loop iterates through each Transaction in the TransactionCollection for a specified Token
        //Adding the details to a Two Dimensional array containing the identifier e.g. Date and the value of said identifier.
        for (Iterator<Transaction> iter = tc.iterator(); iter.hasNext(); ) {
            Transaction tran = iter.next();

            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Date", df.format(tran.Date))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Description", tran.Description)));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Amount", Integer.toString(tran.Amount))));
            twoDArrayList.add(new ArrayList<String>(Arrays.asList("Currency", tran.Currency)));
        }

        Transactions = twoDArrayList;
    }

    public void spend(Date date, String description, int amount, String currency) throws SQLException, ClassNotFoundException {
        //The spend method adds a transaction for a specific Token. Returning a bool representing weather it was added
        //Successfully or not
        DataLayer dl = new DataLayer();
        dl.AddTransaction(Token, (java.sql.Date) date, description, amount, currency);
    }
}
