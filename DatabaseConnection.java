import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    public static final String DB_URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String DB_USER = "sa";
    public static final String DB_PASSWD = "";
    static private Connection conn = null;

    private DatabaseConnection(){}

    public synchronized static Connection getConnection() throws ClassNotFoundException, SQLException
    {
        if(conn == null)
        {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWD);
            return conn;
        }
        else
        {
            return conn;
        }
    }
}
