/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDAO;

import ModelsDTO.Erupcion;
import connection.ConnectionSQLSERVER;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seth
 */
public class ErupcionDAO {
    
    private final String SQL_INSERT_DATA = "{Call SPRegistrarErupcion(?,?,?)}";
    private final String SQL_SELECT_DATA = "{Call SPObtenerErupcionesVolcan(?)}";
    private final String SQL_SELECT_ID = "{Call SPObtenerErupcionPorID(?)}";
    
    public int registrarErupcion(Erupcion erupcion) {
        Connection conn = null;
        CallableStatement cstmt = null;
        int filaAfectada = 0;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_INSERT_DATA);
            
            cstmt.setInt(1, erupcion.getVolcan_ID());
            cstmt.setString(2, erupcion.getTipo());
            cstmt.setInt(3, erupcion.getDuracion_Horas());
            
            System.out.println("Ejecutar consulta insert de erupción");
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
    
    public List<Erupcion> obtenerErupcionesVolcan(int volcanId) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<Erupcion> listaErupciones = new ArrayList<>();
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_DATA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, volcanId);
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                while (rs.next()) {
                    Erupcion erupcion = new Erupcion(
                        rs.getInt("ID"),
                        rs.getInt("Volcan_ID"),
                        rs.getTimestamp("Fecha"),
                        rs.getString("Tipo"),
                        rs.getInt("Duracion_Horas")
                    );
                    listaErupciones.add(erupcion);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(rs);
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return listaErupciones;
    }
    
    public Erupcion obtenerErupcionPorId(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Erupcion erupcion = null;
        
        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_ID, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            cstmt.setInt(1, id);
            
            boolean resultado = cstmt.execute();
            
            if (resultado) {
                rs = cstmt.getResultSet();
                if (rs.next()) {
                    erupcion = new Erupcion(
                        rs.getInt("ID"),
                        rs.getInt("Volcan_ID"),
                        rs.getTimestamp("Fecha"),
                        rs.getString("Tipo"),
                        rs.getInt("Duracion_Horas")
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
        return erupcion;
    }
}