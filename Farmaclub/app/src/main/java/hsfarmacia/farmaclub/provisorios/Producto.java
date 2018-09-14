package hsfarmacia.farmaclub.provisorios;

import android.graphics.Bitmap;

public class Producto {

    private String codigo;
    private String nombre;
    private int puntos;
    private String comentario;
    private Bitmap foto1;

    public Producto(String codigo, String nombre, int puntos, String comentario, Bitmap foto1) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.puntos = puntos;
        this.comentario = comentario;
        this.foto1 = foto1;
    }

    public Producto () {
        this.codigo = "";
        this.nombre = "";
        this.puntos = 0;
        this.comentario = "";
        this.foto1 = null;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Bitmap getFoto1() {
        return foto1;
    }

    public void setFoto1(Bitmap foto1) {
        this.foto1 = foto1;
    }

}
