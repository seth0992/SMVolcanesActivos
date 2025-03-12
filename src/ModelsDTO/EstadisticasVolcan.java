/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ModelsDTO;

import java.math.BigDecimal;

/**
 *
 * @author seth
 */
public class EstadisticasVolcan {
    private String nombreVolcan;
    private String estadoVolcan;
    private int totalSismos;
    private BigDecimal promedioMagnitud;
    private int totalErupciones;
    private int sensoresOperativos;
    private int sensoresFalla;

    public EstadisticasVolcan() {
    }

    public EstadisticasVolcan(String nombreVolcan, String estadoVolcan, int totalSismos, 
                             BigDecimal promedioMagnitud, int totalErupciones, 
                             int sensoresOperativos, int sensoresFalla) {
        this.nombreVolcan = nombreVolcan;
        this.estadoVolcan = estadoVolcan;
        this.totalSismos = totalSismos;
        this.promedioMagnitud = promedioMagnitud;
        this.totalErupciones = totalErupciones;
        this.sensoresOperativos = sensoresOperativos;
        this.sensoresFalla = sensoresFalla;
    }

    // Getters y setters
    public String getNombreVolcan() {
        return nombreVolcan;
    }

    public void setNombreVolcan(String nombreVolcan) {
        this.nombreVolcan = nombreVolcan;
    }

    public String getEstadoVolcan() {
        return estadoVolcan;
    }

    public void setEstadoVolcan(String estadoVolcan) {
        this.estadoVolcan = estadoVolcan;
    }

    public int getTotalSismos() {
        return totalSismos;
    }

    public void setTotalSismos(int totalSismos) {
        this.totalSismos = totalSismos;
    }

    public BigDecimal getPromedioMagnitud() {
        return promedioMagnitud;
    }

    public void setPromedioMagnitud(BigDecimal promedioMagnitud) {
        this.promedioMagnitud = promedioMagnitud;
    }

    public int getTotalErupciones() {
        return totalErupciones;
    }

    public void setTotalErupciones(int totalErupciones) {
        this.totalErupciones = totalErupciones;
    }

    public int getSensoresOperativos() {
        return sensoresOperativos;
    }

    public void setSensoresOperativos(int sensoresOperativos) {
        this.sensoresOperativos = sensoresOperativos;
    }

    public int getSensoresFalla() {
        return sensoresFalla;
    }

    public void setSensoresFalla(int sensoresFalla) {
        this.sensoresFalla = sensoresFalla;
    }
    
    public int getTotalSensores() {
        return sensoresOperativos + sensoresFalla;
    }
}