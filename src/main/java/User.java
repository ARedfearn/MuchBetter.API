import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    public String Token;
    public String Currency;
    public Integer Balance;
    public List<Transaction> Transactions;

    public User() {

    }

    public User(String token) {
        Token = token;
    }

    public void GetDetails() throws SQLException {
        //If no token is passed to the DataLayer a new user is created and it's details are returned.
        DataLayer dl = new DataLayer();
        //The users details are returned from the data layer as a result set. Each property is set from the result set.
        ResultSet rs = dl.GetUser(this);
        while (rs.next()) {
            Token = rs.getString(2);
            Balance = rs.getInt(3);
            Currency = rs.getString(4);
        }
    }

    public void GetTransaction() throws SQLException {
        TransactionCollection tc = new TransactionCollection(Token);
        List<Transaction> transactionList = new ArrayList<>();
        for (Transaction transaction : tc) {
            transactionList.add(transaction);
        }
        Transactions = transactionList;
    }

    public void Spend(Transaction transaction) throws SQLException {
        //The spend method adds a transaction for a specific Token.
        DataLayer dl = new DataLayer();
        dl.AddTransaction(Token, transaction);
    }
}
