package kt.distribuidora.elementos;

public class ClienteNuevo {

    long dni;
    int categoria;
    String nombre;
    String correo;
    String telefono;
    String direccion;
    String localidad;
    int codpos;
    //int fecnac;


    public ClienteNuevo() {
        this.dni = 0;
        this.categoria = 0;
        this.nombre = "";
        this.correo = "";
        this.telefono = "";
        this.direccion = "";
        this.localidad = "";
        this.codpos = 0;
        //this.fecnac = 0;
    }

    public ClienteNuevo(long dni, int categoria, String nombre, String correo, String telefono, String direccion, String localidad, int codpos) {
        this.dni = dni;
        this.categoria = categoria;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.codpos = codpos;
        //this.fecnac = fecnac;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getCodpos() {
        return codpos;
    }

    public void setCodpos(int codpos) {
        this.codpos = codpos;
    }

    /*
    public int getFecnac() {
        return fecnac;
    }

    public void setFecnac(int fecnac) {
        this.fecnac = fecnac;
    }
    */
}
