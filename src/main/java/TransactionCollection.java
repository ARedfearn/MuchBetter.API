import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionCollection implements Iterable<Transaction> {
    private final List<Transaction> list = new ArrayList<Transaction>();
    private String Token;

    public TransactionCollection(String token) throws SQLException {
        //Upon calling this Class it is populated with each transaction for the specified Token
        Token = token;
        GetTransactions();
    }

    private void GetTransactions() throws SQLException {
        DataLayer dl = new DataLayer();
        ResultSet rs = dl.GetTransaction(Token);

        //Each transaction returned from the DataLayer is added to list
        //Iterating through this class will return each Transaction
        while (rs.next()) {
            Transaction t = new Transaction();

            t.Date = rs.getDate(3);
            t.Description = rs.getString(4);
            t.Amount = rs.getInt(5);
            t.Currency = rs.getString(6);

            list.add(t);
        }
    }

    @Override
    public Iterator<Transaction> iterator() {
        return list.iterator();
    }
}
