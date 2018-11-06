package hsfarmacia.farmaclub.provisorios;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class NovedadesVector implements Novedades {
    protected List<Novedad> vectorNovedades;


    public NovedadesVector() {
        // vectorNovedades = new LinkedList<>();
        vectorNovedades = ejemploNovedades();
    }

    //hasta aca el ctrl z

    public NovedadesVector(List<Novedad> lista) {
        vectorNovedades = lista;
    }

    public List<Novedad> getArray(int from, int to){
        return vectorNovedades.subList(from - 1,to - 1);
    }

    public void ResetList() {
        vectorNovedades.clear();
    }


    public Novedad elemento(int id) {
        return vectorNovedades.get(id);
    }

    public void anyade(Novedad novedad) {
        vectorNovedades.add(novedad);
    }

    public int nuevo() {
        Novedad novedad = new Novedad();
        vectorNovedades.add(novedad);
        return vectorNovedades.size()-1;
    }

    public void borrar(int id) {
        vectorNovedades.remove(id);
    }

    public int tamanyo() {
        return vectorNovedades.size();
    }

    public void actualiza(int id, Novedad novedad) {
        vectorNovedades.set(id, novedad);
    }







    public static LinkedList<Novedad> ejemploNovedades() {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        LinkedList<Novedad> novedades = new LinkedList<Novedad>();
        novedades.add(new Novedad("Sorpresa " + day + "/" + month, "15% de descuento en Perfumeria",
                "Este es el detalle de la novedad sobre el Descuento",
                day + "/" + month + "/" + year));
        return novedades;
    }











}
