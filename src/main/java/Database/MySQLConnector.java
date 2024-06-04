package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    private static MySQLConnector instance;
    private final String url = "jdbc:mysql://mysql-java-project-java-project-ary.j.aivencloud.com:19051/defaultdb?useSSL=true";
    private final String username = "avnadmin";
    private final String password = "AVNS_tOOa9Jvf_pEokRY30zo";
    private final Connection connection;

    private MySQLConnector() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex);
        }
    }

    public static MySQLConnector getInstance() throws SQLException {
        if (instance == null) {
            instance = new MySQLConnector();
        } else if (instance.getConnection().isClosed()) {
            instance = new MySQLConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

