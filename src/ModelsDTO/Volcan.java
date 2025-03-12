package ModelsDTO;

/**
 *
 * @author seth
 */
public class Volcan {
 
    private int ID;
    private String Nombre;
    private String Ubicacion;
    private int Altura;
    private String Estado;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }

    public int getAltura() {
        return Altura;
    }

    public void setAltura(int Altura) {
        this.Altura = Altura;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }
    
    public Volcan(){}
    
    public Volcan(String Nombre, String Ubicacion, int Altura) {
        this.Nombre = Nombre;
        this.Ubicacion = Ubicacion;
        this.Altura = Altura;
    }

    public Volcan(int ID, String Nombre, String Ubicacion, int Altura, String Estado) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Ubicacion = Ubicacion;
        this.Altura = Altura;
        this.Estado = Estado;
    }    
}
