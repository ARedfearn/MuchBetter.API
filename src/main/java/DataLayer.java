import java.sql.*;
import java.util.Date;

    /*
        The DataLayer accessed the H2 database and executes INSERT, UPDATE and SELECT Statements to Retrieve details from
        the User table and the Transaction table under the MUCHBETTER_API Schema. The data is returned as a result set
        and passed to the calling class.
    */


public class DataLayer {
    //A tcp connection is used as it allows multiple connections to the data base at once
    private Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");

    public DataLayer() throws SQLException {

    }

    public ResultSet GetUser(User user) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");

        String token = user.Token;
        if(user.Token == null){
            //If no Token is passed we need to add a new User. This is the requirement of the /login endpoint.
            token = AddUser();
        }

        String sqlSelect = ("SELECT * FROM MUCHBETTER_API.USERS " +
                            "WHERE TOKEN = ?");

        PreparedStatement statement = connection.prepareStatement(sqlSelect);
        statement.setString(1, token);
        ResultSet rs = statement.executeQuery();

        return rs;
    }

    public ResultSet GetTransaction(String token) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");

        String sqlSelect = ("SELECT * FROM MUCHBETTER_API.TRANSACTIONS " +
                            "WHERE TOKEN = ?");

        PreparedStatement statement = connection.prepareStatement(sqlSelect);
        statement.setString(1, token);
        ResultSet rs = statement.executeQuery();

        return rs;
    }


    public String AddUser() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        String sqlInsert = ("INSERT INTO MUCHBETTER_API.USERS (TOKEN, BALANCE, CURRENCY) " +
                            "VALUES (RANDOM_UUID(), 0, 'GBP')");

        int id;
        //The Generated_Key is the newly inserted UserID column within the User table in the H2 Database.
        //From the UserID we can retrieve the Token for this new User.
        PreparedStatement statement = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate();
        {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    //The UserID is returned as the generated key rather than any other column as it is the PRIMARY KEY
                    //Column, it has an auto_increments by 1.
                    id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }

        //We retrieve the Token from the ID.
        String token = GetTokenFromID(id);

        //Once we have the Token we add an initial transaction for the new User.
        Date date = new Date();
        java.sql.Date today = new java.sql.Date(date.getTime());
        AddTransaction(token, today, "Initial Transaction", 100, "GBP");
        return token;
    }

    private String GetTokenFromID(int id) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");

        String sqlSelect = ("SELECT TOKEN FROM MUCHBETTER_API.USERS " +
                            "WHERE USERID = ?");

        PreparedStatement statement = connection.prepareStatement(sqlSelect);
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        String token = "";
        while(rs.next()){
            token = rs.getString(1);
        }
        return token;
    }

    public void AddTransaction(String token, Date date, String description, int amount, String currency) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");

        String sqlInsert = ("INSERT INTO MUCHBETTER_API.TRANSACTIONS (TOKEN, TRANDATE, DESCRIPTION, AMOUNT, CURRENCY) " +
                            "VALUES (?, ?, ?, ?, ?)");

        PreparedStatement statement = connection.prepareStatement(sqlInsert);
        statement.setString(1, token);
        statement.setDate(2, (java.sql.Date) date);
        statement.setString(3, description);
        statement.setInt(4, amount);
        statement.setString(5, currency);

        statement.executeUpdate();

        //Upon adding any transaction we must update the users balance.
        UpdateBalance(token);
    }

    private void UpdateBalance(String token) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");

        //The balance is updated with the following CASE statement. SUM() Alone will not correctly return the total value
        //of said users balance if negative values are present. So instead we get a total of the negative transactions and
        //subtract them from the total value of the positive transactions.
        String sqlUpdate = ("UPDATE MUCHBETTER_API.USERS " +
                "SET BALANCE = " +
                "(SELECT SUM(CASE WHEN AMOUNT > 0 THEN AMOUNT ELSE 0 END) - SUM(CASE WHEN AMOUNT < 0 THEN ABS(AMOUNT) ELSE 0 END) AS SUM " +
                "FROM MUCHBETTER_API.TRANSACTIONS " +
                "WHERE TOKEN = ?)" +
                "WHERE TOKEN = ?");

        PreparedStatement statement = connection.prepareStatement(sqlUpdate);
        statement.setString(1, token);
        statement.setString(2, token);
        statement.executeUpdate();
    }
}
