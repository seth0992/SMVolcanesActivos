/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDTO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author seth
 */
public class Erupcion {
    private int ID;
    private int Volcan_ID;
    private Timestamp Fecha;
    private String Tipo;
    private int Duracion_Horas;

    public Erupcion() {
    }

    public Erupcion(int Volcan_ID, String Tipo, int Duracion_Horas) {
        this.Volcan_ID = Volcan_ID;
        this.Tipo = Tipo;
        this.Duracion_Horas = Duracion_Horas;
    }

    public Erupcion(int ID, int Volcan_ID, Timestamp Fecha, String Tipo, int Duracion_Horas) {
        this.ID = ID;
        this.Volcan_ID = Volcan_ID;
        this.Fecha = Fecha;
        this.Tipo = Tipo;
        this.Duracion_Horas = Duracion_Horas;
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

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public int getDuracion_Horas() {
        return Duracion_Horas;
    }

    public void setDuracion_Horas(int Duracion_Horas) {
        this.Duracion_Horas = Duracion_Horas;
    }
    
    @Override
    public String toString() {
        return "Erupción [ID=" + ID + ", Fecha=" + getFechaFormateada() + ", Tipo=" + Tipo + 
               ", Duración=" + Duracion_Horas + " horas]";
    }
}