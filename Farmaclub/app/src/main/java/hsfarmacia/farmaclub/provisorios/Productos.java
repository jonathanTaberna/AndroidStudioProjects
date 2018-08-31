package hsfarmacia.farmaclub.provisorios;

public interface Productos {
    Producto elemento(int id); //Devuelve el elemento dado su id
    void anyade(Producto lugar); //Añade el elemento indicado
    int nuevo(); //Añade un elemento en blanco y devuelve su id
    void borrar(int id); //Elimina el elemento con el id indicado
    int tamanyo(); //Devuelve el número de elementos
    void actualiza(int id, Producto lugar); //Reemplaza un elemento
}
