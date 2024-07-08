package kt.distribuidoraBJ.elementos;

public class Articulo {

    int codigo;
    String descripcion;
    double costo;
    int codigoLista;
    double precio;
    int cantidad;
    int cantidadBonif;

    public Articulo() {
        this.codigo = 0;
        this.descripcion = "";
        this.costo = 0;
        this.codigoLista = 0;
        this.precio = 0;
        this.cantidad = 0;
        this.cantidadBonif = 0;
    }

    public Articulo(int codigo, String descripcion, double costo, int codigoLista, double precio, int cantidad, int cantidadBonif) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.costo = costo;
        this.codigoLista = codigoLista;
        this.precio = precio;
        this.cantidad = cantidad;
        this.cantidadBonif = cantidadBonif;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getCodigoLista() {
        return codigoLista;
    }

    public void setCodigoLista(int codigoLista) {
        this.codigoLista = codigoLista;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadBonif() {
        return cantidadBonif;
    }

    public void setCantidadBonif(int cantidadBonif) {
        this.cantidadBonif = cantidadBonif;
    }
}
