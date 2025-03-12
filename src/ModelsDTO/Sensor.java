/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDTO;

/**
 *
 * @author seth
 */
public class Sensor {
    private int ID;
    private int Volcan_ID;
    private String Ubicacion;
    private String Estado;

    public Sensor() {
    }

    public Sensor(int Volcan_ID, String Ubicacion) {
        this.Volcan_ID = Volcan_ID;
        this.Ubicacion = Ubicacion;
        this.Estado = "Operativo"; // Estado por defecto
    }

    public Sensor(int ID, int Volcan_ID, String Ubicacion, String Estado) {
        this.ID = ID;
        this.Volcan_ID = Volcan_ID;
        this.Ubicacion = Ubicacion;
        this.Estado = Estado;
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

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }
    
    @Override
    public String toString() {
        return "Sensor [ID=" + ID + ", Ubicación=" + Ubicacion + ", Estado=" + Estado + "]";
    }
}