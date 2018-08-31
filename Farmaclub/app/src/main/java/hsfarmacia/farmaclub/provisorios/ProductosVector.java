package hsfarmacia.farmaclub.provisorios;

import java.util.ArrayList;
import java.util.List;

public class ProductosVector implements Productos {
    protected List<Producto> vectorLugares = ejemploProductos();

    public ProductosVector() {
        vectorLugares = ejemploProductos();
    }

    public Producto elemento(int id) {
        return vectorLugares.get(id);
    }

    public void anyade(Producto lugar) {
        vectorLugares.add(lugar);
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


    public static ArrayList<Producto> ejemploProductos() {
        ArrayList<Producto> productos = new ArrayList<Producto>();
        productos.add(new Producto("IBU PROFENO", "ibu profeno pa kurar to tus males beibi",125, "Sin foto"));
        productos.add(new Producto("ALIKAL", "alikal pa kurar tus borracheras piyin",125, "Sin foto"));
        productos.add(new Producto("EL NEGRO IONS", "el negro ions pa llevarte por el mal kamino bro",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 4", "DESCRIPCION 4",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 5", "DESCRIPCION 5",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 6", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 7", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 8", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 9", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 10", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 11", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 12", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 13", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 14", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 15", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 16", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 17", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 18", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 19", "DESCRIPCION 6",125, "Sin foto"));
        productos.add(new Producto("PRODUCTO 20", "DESCRIPCION 6",125, "Sin foto"));
        return productos;
    }
}
