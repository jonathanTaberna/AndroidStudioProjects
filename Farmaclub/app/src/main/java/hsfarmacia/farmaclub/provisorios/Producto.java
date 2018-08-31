package hsfarmacia.farmaclub.provisorios;

public class Producto {

    private String titulo;
    private String descripcion;
    private int puntos;
    private String foto;

    public Producto(String titulo, String descripcion, int puntos, String foto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.foto = foto;
    }

    public  Producto () {
        this.titulo = "";
        this.descripcion = "";
        this.puntos = 0;
        this.foto = "";
    }

    @Override
    public String toString() {
        return "Producto{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", puntos=" + puntos +
                ", foto='" + foto + '\'' +
                '}';
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
