package kt.distribuidora.elementos;

public class Cliente {

    long codigo;
    String nombre;
    int codigoLista;
    double costo;
    Boolean facturaConLista;
    int zona;

    public Cliente(long codigo, String nombre, int codigoLista, double costo, Boolean facturaConLista, int zona) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.codigoLista = codigoLista;
        this.costo = costo;
        this.facturaConLista = facturaConLista;
        this.zona = zona;
    }

    public Cliente() {
        this.codigo = 0;
        this.nombre = "";
        this.codigoLista = 0;
        this.costo = 0;
        this.facturaConLista = false;
        this.zona = 0;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigoLista() {
        return codigoLista;
    }

    public void setCodigoLista(int codigoLista) {
        this.codigoLista = codigoLista;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Boolean getFacturaConLista() {
        return facturaConLista;
    }

    public void setFacturaConLista(Boolean facturaConLista) {
        this.facturaConLista = facturaConLista;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }
}