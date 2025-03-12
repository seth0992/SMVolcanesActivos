/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDAO;

import ModelsDTO.Volcan;
import connection.ConnectionSQLSERVER;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seth
 */
public class VolcanDAO {

    private final String SQL_SELECT_DATA = "{Call SPObtenerVolcanesConFiltro(?,?,?)}";
    private final String SQL_INSERT_DATA = "{Call SPInsertarVolcan(?,?,?)}";
    private final String SQL_UPDATE_DATA = "{Call SPActualizarVolcan(?,?,?,?,?)}";
    private final String SQL_DELETE_DATA = "{Call SPEliminarVolcan(?)}";
    private final String SQL_UPDATE_EST = "{Call SPActualizarVolcanEstado(?,?)}";
    private final String SQL_SELECT_ID = "{Call SPObtenerVolcanesPorID(?)}";
            
    public int registrarVolcan(Volcan volcan) {
        Connection conn = null;
        CallableStatement cstmt = null;

        int filaAfectada = 0;

        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_INSERT_DATA);

            cstmt.setString(1, volcan.getNombre());
            cstmt.setString(2, volcan.getUbicacion());
            cstmt.setInt(3, volcan.getAltura());

            System.out.println("Ejecutar consulta insert de volcan");
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

    public int modificarVolcan(Volcan volcan) {
        Connection conn = null;
        CallableStatement cstmt = null;

        int filaAfectada = 0;

        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_UPDATE_DATA);
          
            cstmt.setInt(1, volcan.getID());
            cstmt.setString(2, volcan.getNombre());
            cstmt.setString(3, volcan.getUbicacion());
            cstmt.setInt(4, volcan.getAltura());
            cstmt.setString(5, volcan.getEstado());
            

            System.out.println("Ejecutar consulta update de volcan");
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
    
    public int eliminarVolcan(int id) {
        Connection conn = null;
        CallableStatement cstmt = null;

        int filaAfectada = 0;

        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_DELETE_DATA);
          
            cstmt.setInt(1, id);     
           
            System.out.println("Ejecutar consulta delete de volcan");
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
    
    public List<Volcan> obtenerVolcanesConFiltro(String nombre, String estado, Integer altura) {
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        List<Volcan> listaVolcanes = new ArrayList<>();

        try {
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_DATA, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            //Enviar los parametros 
            cstmt.setString(1, nombre);
            cstmt.setString(2, estado);
            if (altura != null) {
                cstmt.setInt(3, altura);
            } else {
                cstmt.setNull(3, Types.INTEGER);
            }

            //Ejecutar la consulta 
            System.out.println("Ejecutando la consulta de volcanes");
            boolean resultado = cstmt.execute();

            if (resultado) {
                rs = cstmt.getResultSet();
                while (rs.next()) {

                    Volcan volcan = new Volcan(
                            rs.getInt("ID"),
                            rs.getString("Nombre"),
                            rs.getString("Ubicacion"),
                            rs.getInt("Altura"),
                            rs.getString("Estado")
                    );
                    listaVolcanes.add(volcan);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionSQLSERVER.close(rs);
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        return listaVolcanes;
    }

    public Volcan obtenerVolcanPorId(int id){
        Volcan volcan = new Volcan();
        Connection conn = null;
        ResultSet rs = null;
        CallableStatement cstmt = null;
        try{
            conn = ConnectionSQLSERVER.getConnection();
            cstmt = conn.prepareCall(SQL_SELECT_ID,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            cstmt.setInt(1, id);
            
            boolean resultado =cstmt.execute();
            if(resultado){
                rs = cstmt.getResultSet();
                while(rs.next()){
                    volcan.setID(rs.getInt("ID"));
                    volcan.setNombre(rs.getString("Nombre"));
                    volcan.setUbicacion(rs.getString("Ubicacion"));
                    volcan.setAltura(rs.getInt("Altura"));
                    volcan.setEstado(rs.getString("Estado"));
                }
            }            
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            ConnectionSQLSERVER.close(rs);
            ConnectionSQLSERVER.close(cstmt);
            ConnectionSQLSERVER.close(conn);
        }
        
        return volcan;
    }
            
}
