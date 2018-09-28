package hsfarmacia.farmaclub.provisorios;

public class Novedad {

    private String nombre;
    private String descripcion;
    private String detalle;
    private String fecha;

    public Novedad(String nombre, String descripcion, String detalle, String fecha) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.detalle = detalle;
        this.fecha = fecha;
    }

    public Novedad() {
        this.nombre = "";
        this.descripcion = "";
        this.detalle = "";
        this.fecha = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
