/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDAO;


import ModelsDTO.Sismo;
import connection.ConnectionSQLSERVER;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seth
 */
public class SismoDAO {
    
    private final String SQL_INSERT_DATA = "{Call SPRegistrarSismo(?,?,?)}";
    private final String SQL_SELECT_DATA = "{Call SPObtenerSismosVolcan(?,?,?)}";
    private final String SQL_SELECT_ID = "{Call SPObtenerSismoPorID(?)}";
    
    public int registrarSismo(Sismo sismo) {
        Connection conn = null;
        CallableStatement cstmt = null;
        int filaAfectada = 0;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_INSERT_DATA);
            
            cstmt.setInt(1, sismo.getVolcan_ID());
            cstmt.setBigDecimal(2, sismo.getMagnitud());
            cstmt.setInt(3, sismo.getProfundidad());
            
            System.out.println("Ejecutar consulta insert de sismo");
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
    
    public List<Sismo> obtenerSismosVolcan(int volcanId, Date fechaInicio, Date fechaFin) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<Sismo> listaSismos = new ArrayList<>();
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_DATA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, volcanId);
            if (fechaInicio != null) {
                cstmt.setDate(2, fechaInicio);
            } else {
                cstmt.setNull(2, Types.DATE);
            }
            
            if (fechaFin != null) {
                cstmt.setDate(3, fechaFin);
            } else {
                cstmt.setNull(3, Types.DATE);
            }
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                while (rs.next()) {
                    Sismo sismo = new Sismo(
                        rs.getInt("ID"),
                        rs.getInt("Volcan_ID"),
                        rs.getTimestamp("Fecha"),
                        rs.getBigDecimal("Magnitud"),
                        rs.getInt("Profundidad")
                    );
                    listaSismos.add(sismo);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(rs);
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return listaSismos;
    }
    
    public Sismo obtenerSismoPorId(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Sismo sismo = null;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, id);
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                if (rs.next()) {
                    sismo = new Sismo(
                        rs.getInt("ID"),
                        rs.getInt("Volcan_ID"),
                        rs.getTimestamp("Fecha"),
                        rs.getBigDecimal("Magnitud"),
                        rs.getInt("Profundidad")
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
        return sismo;
    }
}