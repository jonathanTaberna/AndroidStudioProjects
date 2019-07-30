package hsneoclinica.neoclinica.provisorios;

import java.io.Serializable;

public class Check implements Serializable {
    public int id;
    public String nombre;
    public Boolean marcado;

    public Check(int id, String nombre, Boolean marcado) {
        this.id = id;
        this.nombre = nombre;
        this.marcado = marcado;
    }

}
