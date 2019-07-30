package hsneoclinica.neoclinica.provisorios;

import java.util.LinkedList;
import java.util.List;

public class TurnosVector implements Turnos {
    //protected List<Turno> vectorTurnos = ejemploProductos();
    protected List<Turno> vectorTurnos;

    public TurnosVector() {
        //vectorTurnos = ejemploProductos();
        vectorTurnos = new LinkedList<>();
    }

    //hasta aca el ctrl z

    public TurnosVector(List<Turno> lista) {
        vectorTurnos = lista;
    }

    public List<Turno> getArray(int from, int to){
        return vectorTurnos.subList(from - 1,to);
    }

    public void ResetList() {
        vectorTurnos.clear();
    }


    ////////////////////////////

    public Turno elemento(int id) {
        return vectorTurnos.get(id);
    }

    public void anyade(Turno turno) {
        vectorTurnos.add(turno);
    }

    public int nuevo() {
        Turno turno = new Turno();
        vectorTurnos.add(turno);
        return vectorTurnos.size()-1;
    }

    public void borrar(int id) {
        vectorTurnos.remove(id);
    }

    public int tamanyo() {
        return vectorTurnos.size();
    }

    public void actualiza(int id, Turno turno) {
        vectorTurnos.set(id, turno);
    }

    public void eliminaListaProductos() {
        vectorTurnos.clear(); vectorTurnos = null;}

}
