/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDAO;

import ModelsDTO.EstadisticasVolcan;
import connection.ConnectionSQLSERVER;
import java.sql.*;
import java.math.BigDecimal;

/**
 *
 * @author seth
 */
public class EstadisticasDAO {
    
    private final String SQL_ESTADISTICAS = "{Call SPObtenerEstadisticasVolcan(?)}";
    
    public EstadisticasVolcan obtenerEstadisticasVolcan(int volcanId) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        EstadisticasVolcan estadisticas = null;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_ESTADISTICAS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, volcanId);
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                if (rs.next()) {
                    estadisticas = new EstadisticasVolcan(
                        rs.getString("NombreVolcan"),
                        rs.getString("EstadoVolcan"),
                        rs.getInt("TotalSismos"),
                        rs.getBigDecimal("PromedioMagnitud"),
                        rs.getInt("TotalErupciones"),
                        rs.getInt("SensoresOperativos"),
                        rs.getInt("SensoresFalla")
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
        return estadisticas;
    }
}