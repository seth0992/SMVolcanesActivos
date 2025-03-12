package connection;

import java.sql.*;

/**
 *
 * @author seth
 */
public class ConnectionSQLSERVER {

    private static String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String JDBC_DB = "db_SM_VolcanesActivos";
    private static String JDBC_USER = "user";
    private static String JDBC_PASS = "1234";
    private static String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=" + JDBC_DB + ";encrypt=false";
    private static Driver driver = null;

    public static synchronized Connection getConnection() throws SQLException {
        if (driver == null) {
            try {
                //Se carga el driver 
                Class jdbcDriverClass = Class.forName(JDBC_DRIVER);

                driver = (Driver) jdbcDriverClass.getDeclaredConstructor().newInstance();

                //Utilizarmos el driver para registrar el contrador
                DriverManager.registerDriver(driver);
            } catch (Exception ex) {
                System.out.println("Fallo al cargar el driver");
                ex.printStackTrace();
            }
        }
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void close(PreparedStatement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
      public static void close(CallableStatement cstmt) {
        try {
            if (cstmt != null) {
                cstmt.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
