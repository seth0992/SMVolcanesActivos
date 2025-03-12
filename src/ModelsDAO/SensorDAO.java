/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDAO;
import ModelsDTO.Sensor;
import connection.ConnectionSQLSERVER;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seth
 */
public class SensorDAO {
    
    private final String SQL_INSERT_DATA = "{Call SPInsertarSensor(?,?)}";
    private final String SQL_UPDATE_DATA = "{Call SPActualizarSensor(?,?,?)}";
    private final String SQL_DELETE_DATA = "{Call SPEliminarSensor(?)}";
    private final String SQL_SELECT_DATA = "{Call SPObtenerSensoresVolcan(?)}";
    private final String SQL_SELECT_ID = "{Call SPObtenerSensorPorID(?)}";
    private final String SQL_REPARAR_SENSOR = "{Call SPRepararSensor(?)}";
    
    public int registrarSensor(Sensor sensor) {
        Connection conn = null;
        CallableStatement cstmt = null;
        int filaAfectada = 0;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_INSERT_DATA);
            
            cstmt.setInt(1, sensor.getVolcan_ID());
            cstmt.setString(2, sensor.getUbicacion());
            
            System.out.println("Ejecutar consulta insert de sensor");
            cstmt.execute();
            filaAfectada = cstmt.getUpdateCount();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return filaAfectada;
    }
    
    public int actualizarSensor(Sensor sensor) {
        Connection conn = null;
        CallableStatement cstmt = null;
        int filaAfectada = 0;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_UPDATE_DATA);
            
            cstmt.setInt(1, sensor.getID());
            cstmt.setString(2, sensor.getUbicacion());
            cstmt.setString(3, sensor.getEstado());
            
            System.out.println("Ejecutar consulta update de sensor");
            cstmt.execute();
            filaAfectada = cstmt.getUpdateCount();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return filaAfectada;
    }
    
    public int eliminarSensor(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;
        int filaAfectada = 0;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_DELETE_DATA);
            
            cstmt.setInt(1, id);
            
            System.out.println("Ejecutar consulta delete de sensor");
            cstmt.execute();
            filaAfectada = cstmt.getUpdateCount();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return filaAfectada;
    }
    
    public List<Sensor> obtenerSensoresVolcan(int volcanId) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<Sensor> listaSensores = new ArrayList<>();
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_DATA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, volcanId);
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                while (rs.next()) {
                    Sensor sensor = new Sensor(
                        rs.getInt("ID"),
                        rs.getInt("Volcan_ID"),
                        rs.getString("Ubicacion"),
                        rs.getString("Estado")
                    );
                    listaSensores.add(sensor);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(rs);
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return listaSensores;
    }
    
    public Sensor obtenerSensorPorId(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Sensor sensor = null;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, id);
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                if (rs.next()) {
                    sensor = new Sensor(
                        rs.getInt("ID"),
                        rs.getInt("Volcan_ID"),
                        rs.getString("Ubicacion"),
                        rs.getString("Estado")
                    );
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(rs);
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return sensor;
    }
    
    public int repararSensor(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;
        int filaAfectada = 0;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_REPARAR_SENSOR);
            
            cstmt.setInt(1, id);
            
            System.out.println("Ejecutar procedimiento de reparación de sensor");
            cstmt.execute();
            filaAfectada = cstmt.getUpdateCount();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return filaAfectada;
    }
}
