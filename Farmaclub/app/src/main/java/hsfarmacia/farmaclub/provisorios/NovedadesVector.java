package hsfarmacia.farmaclub.provisorios;

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
        LinkedList<Novedad> novedades = new LinkedList<Novedad>();
        novedades.add(new Novedad("Sorpresa 20/09", "30% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "20/09/2018"));
        novedades.add(new Novedad("Sorpresa 21/09", "40% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "21/09/2018"));
        novedades.add(new Novedad("Sorpresa 22/09", "50% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "22/09/2018"));
        novedades.add(new Novedad("Sorpresa 23/09", "60% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "23/09/2018"));
        novedades.add(new Novedad("Sorpresa 24/09", "70% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "24/09/2018"));
        novedades.add(new Novedad("Sorpresa 25/09", "80% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "25/09/2018"));
        novedades.add(new Novedad("Sorpresa 26/09", "90% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "26/09/2018"));
        novedades.add(new Novedad("Sorpresa 27/09", "100% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "27/09/2018"));
        novedades.add(new Novedad("Sorpresa 28/09", "110% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "28/09/2018"));
        novedades.add(new Novedad("Sorpresa 29/09", "120% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "29/09/2018"));
        novedades.add(new Novedad("Sorpresa 30/09", "130% de descuento en merka loko, aguante el pity y batman",
                "Este es el detalle de la novedad",
                "30/09/2018"));
        return novedades;
    }











}
