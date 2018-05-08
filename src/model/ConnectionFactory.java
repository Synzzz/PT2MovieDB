package model;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Creates a connection to the local mysql server with predefined username and password * 
 * @author Ati
 */
public class ConnectionFactory {
    private static MysqlConnectionPoolDataSource conn;
    
    private ConnectionFactory(){}
    
    /**
     * Initializes the database connection,throws exception if not possible
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() 
        throws ClassNotFoundException, SQLException{
        if (conn == null){
            Class.forName("com.mysql.jdbc.Driver");
            conn = new MysqlConnectionPoolDataSource();
            conn.setServerName("localhost");
            conn.setPort(3306);
            conn.setDatabaseName("moviedb?&useSSL=false");
            conn.setUser("root");
            conn.setPassword("root");
        }
        return conn.getPooledConnection().getConnection();
    }
    /*
    public static Connection getConnection() 
        throws ClassNotFoundException, SQLException{
        if (conn == null){
            Class.forName("com.mysql.jdbc.Driver");
            conn = new MysqlConnectionPoolDataSource();
            conn.setServerName("sql7235725");
            conn.setPort(3306);
            conn.setURL("jdbc:mysql://sql7.freemysqlhosting.net/sql7235725?");
            conn.setDatabaseName("sql7235725?&useSSL=false");
            conn.setUser("sql7235725");
            conn.setPassword("MQDwPp9ssm");
        }
        return conn.getPooledConnection().getConnection();
    }
    */
}
