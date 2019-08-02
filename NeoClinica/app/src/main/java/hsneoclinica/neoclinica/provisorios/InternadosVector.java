package hsneoclinica.neoclinica.provisorios;

import java.util.LinkedList;
import java.util.List;

public class InternadosVector {
    protected List<Internado> vectorInternados;

    public InternadosVector() {
        vectorInternados = new LinkedList<>();
    }

    public InternadosVector(List<Internado> lista) {
        vectorInternados = lista;
    }

    public List<Internado> getArray(int from, int to){
        return vectorInternados.subList(from - 1,to);
    }

    public void ResetList() {
        vectorInternados.clear();
    }

    public Internado elemento(int id) {
        return vectorInternados.get(id);
    }

    public void anyade(Internado internado) {
        vectorInternados.add(internado);
    }

    public int nuevo() {
        Internado internado = new Internado();
        vectorInternados.add(internado);
        return vectorInternados.size()-1;
    }

    public void borrar(int id) {
        vectorInternados.remove(id);
    }

    public int tamanyo() {
        return vectorInternados.size();
    }

    public void actualiza(int id, Internado internado) {
        vectorInternados.set(id, internado);
    }

    public void eliminaListaInternados() {
        vectorInternados.clear(); vectorInternados = null;}
}
