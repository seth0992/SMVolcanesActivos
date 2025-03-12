/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author seth
 */
public class Sismo {
    private int ID;
    private int Volcan_ID;
    private Timestamp Fecha;
    private BigDecimal Magnitud;
    private int Profundidad;

    public Sismo() {
    }

    public Sismo(int Volcan_ID, BigDecimal Magnitud, int Profundidad) {
        this.Volcan_ID = Volcan_ID;
        this.Magnitud = Magnitud;
        this.Profundidad = Profundidad;
    }

    public Sismo(int ID, int Volcan_ID, Timestamp Fecha, BigDecimal Magnitud, int Profundidad) {
        this.ID = ID;
        this.Volcan_ID = Volcan_ID;
        this.Fecha = Fecha;
        this.Magnitud = Magnitud;
        this.Profundidad = Profundidad;
    }

    // Getters y setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getVolcan_ID() {
        return Volcan_ID;
    }

    public void setVolcan_ID(int Volcan_ID) {
        this.Volcan_ID = Volcan_ID;
    }

    public Timestamp getFecha() {
        return Fecha;
    }
    
    public String getFechaFormateada() {
        if (Fecha == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(Fecha);
    }

    public void setFecha(Timestamp Fecha) {
        this.Fecha = Fecha;
    }

    public BigDecimal getMagnitud() {
        return Magnitud;
    }

    public void setMagnitud(BigDecimal Magnitud) {
        this.Magnitud = Magnitud;
    }

    public int getProfundidad() {
        return Profundidad;
    }

    public void setProfundidad(int Profundidad) {
        this.Profundidad = Profundidad;
    }
    
    @Override
    public String toString() {
        return "Sismo [ID=" + ID + ", Fecha=" + getFechaFormateada() + ", Magnitud=" + Magnitud + ", Profundidad=" + Profundidad + "km]";
    }
}