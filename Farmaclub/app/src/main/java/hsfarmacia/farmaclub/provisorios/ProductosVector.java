package hsfarmacia.farmaclub.provisorios;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductosVector implements Productos {
    //protected List<Producto> vectorLugares = ejemploProductos();
    protected List<Producto> vectorLugares;

    public ProductosVector() {
        //vectorLugares = ejemploProductos();
        vectorLugares = new LinkedList<>();
    }

    //hasta aca el ctrl z

    public ProductosVector(List<Producto> lista) {
        vectorLugares = lista;
    }

    public List<Producto> getArray(int from, int to){
        return vectorLugares.subList(from - 1,to - 1);
    }

    public void ResetList() {
        vectorLugares.clear();
    }


    ////////////////////////////

    public Producto elemento(int id) {
        return vectorLugares.get(id);
    }

    public void anyade(Producto producto) {
        vectorLugares.add(producto);
    }

    public int nuevo() {
        Producto producto = new Producto();
        vectorLugares.add(producto);
        return vectorLugares.size()-1;
    }

    public void borrar(int id) {
        vectorLugares.remove(id);
    }

    public int tamanyo() {
        return vectorLugares.size();
    }

    public void actualiza(int id, Producto producto) {
        vectorLugares.set(id, producto);
    }

}
