import java.sql.*;
import java.time.LocalDate;

    /*
        The DataLayer accessed the H2 database and executes INSERT, UPDATE and SELECT Statements to Retrieve details from
        the User table and the Transaction table under the MUCHBETTER_API Schema. The data is returned as a result set
        and passed to the calling class.
    */

public class DataLayer {

    private Class Driver;
    {
        try {
             Driver = Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DataLayer() {

    }

    public ResultSet GetUser(User user) throws SQLException {
        ResultSet rs = null;

        String token = user.Token;
        if(user.Token == null){
            //If no Token is passed we need to add a new User.
            token = AddUser();
        }

        String sqlSelect = ("SELECT * FROM MUCHBETTER_API.USERS " +
                            "WHERE TOKEN = ?");

        //Connecting to the H2 database, a tcp connection is used as this allows multiple connections at once.
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlSelect);
        statement.setString(1, token);

        //The statement returns a table(result set).
        rs = statement.executeQuery();

        return rs;
    }

    public ResultSet GetTransaction(String token) throws SQLException {
        ResultSet rs = null;

        String sqlSelect = ("SELECT * FROM MUCHBETTER_API.TRANSACTIONS " +
                            "WHERE TOKEN = ?");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlSelect);
        statement.setString(1, token);
        rs = statement.executeQuery();

        return rs;
    }

    private String AddUser() throws SQLException {
        int id = 0;

        String sqlInsert = ("INSERT INTO MUCHBETTER_API.USERS (TOKEN, BALANCE, CURRENCY) " +
                            "VALUES (RANDOM_UUID(), 0, 'GBP')");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                //The new UserID is returned as the generated key
                id = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        //We retrieve the Token from the ID.
        String token = GetTokenFromID(id);

        //Once we have the Token we add an initial transaction for the new User.
        LocalDate locald = LocalDate.now();
        java.sql.Date today = java.sql.Date.valueOf(locald);

        Transaction transaction = new Transaction();
        transaction.Date = today;
        transaction.Description = "Initial Transaction";
        transaction.Currency = "GBP";
        transaction.Amount = 100;
        AddTransaction(token, transaction);
        return token;
    }

    private String GetTokenFromID(int id) throws SQLException {
        String token = "";

        String sqlSelect = ("SELECT TOKEN FROM MUCHBETTER_API.USERS " +
                            "WHERE USERID = ?");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlSelect);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            token = rs.getString(1);
        }

        return token;
    }

    public void AddTransaction(String token, Transaction transaction) throws SQLException {
        String sqlInsert = ("INSERT INTO MUCHBETTER_API.TRANSACTIONS (TOKEN, TRANDATE, DESCRIPTION, AMOUNT, CURRENCY) " +
                            "VALUES (?, ?, ?, ?, ?)");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, token);

        java.util.Date utilDate = transaction.Date;
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        statement.setDate(2, sqlDate);
        statement.setString(3, transaction.Description);
        statement.setInt(4, transaction.Amount);
        statement.setString(5, transaction.Currency);

        statement.executeUpdate();

        int id;
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                //The new TransactionID is returned as the generated key;
                id = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Adding transaction failed, no ID obtained.");
            }
        }

        UpdateBalance(token, id);
    }

    private void UpdateBalance(String token, int id) throws SQLException {
        //The balance is updated by summing together the balance of the user and the last transaction
        String sqlUpdate = ("UPDATE MUCHBETTER_API.USERS " +
                            "SET BALANCE = BALANCE + (SELECT AMOUNT " +
                                                     "FROM MUCHBETTER_API.TRANSACTIONS " +
                                                     "WHERE TRANSACTIONID = ?) " +
                            "WHERE TOKEN = ?");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlUpdate);
        statement.setInt(1, id);
        statement.setString(2, token);

        statement.executeUpdate();
    }
}
