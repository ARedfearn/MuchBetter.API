import java.sql.*;

public class CleanDatabase {
    private Class Driver;
    {
        try {
            Driver = Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String Token;

    public CleanDatabase(String token) throws SQLException {
        Token = token;
    }

    public void DeleteTransaction() throws SQLException {
        String sqlDelete = ("DELETE FROM MUCHBETTER_API.TRANSACTIONS " +
                            "WHERE TOKEN = ?");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlDelete, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, Token);
        statement.executeUpdate();
    }

    public void DeleteUser() throws SQLException{
        String sqlDelete = ("DELETE FROM MUCHBETTER_API.USERS " +
                            "WHERE TOKEN = ?");

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sqlDelete, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, Token);
        statement.executeUpdate();
    }

}
