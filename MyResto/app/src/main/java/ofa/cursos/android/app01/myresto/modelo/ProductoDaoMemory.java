package ofa.cursos.android.app01.myresto.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ProductoDaoMemory implements ProductoDAO {
    private static List<ProductoMenu> MENU_PRODUCTOS;

    @Override
    public List<ProductoMenu> listarMenu() {
        if(MENU_PRODUCTOS==null){
            MENU_PRODUCTOS = new ArrayList<>();
        }
        return MENU_PRODUCTOS;
    }
    @Override
    public void cargarDatos(String[] datos) {
        MENU_PRODUCTOS = new ArrayList<>();
        for(String unaFila : datos){
                    StringTokenizer tokens = new StringTokenizer(unaFila, "|");
            ProductoMenu aux = new ProductoMenu();
            Integer id = Integer.valueOf(tokens.nextToken().toString());
            String nombre = tokens.nextToken().toString();
            Double precio = Double.valueOf(tokens.nextToken().toString());
            aux.setId(id);
            aux.setNombre(nombre);
            aux.setPrecio(precio);
            MENU_PRODUCTOS.add(aux);
        }
    }
}
