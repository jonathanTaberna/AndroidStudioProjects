package jt.cotitagliero.provisorios;

public interface Clientes {
    Cliente elemento(int id); //Devuelve el elemento dado su id
    void anyade(Cliente cliente); //Añade el elemento indicado
    int nuevo(); //Añade un elemento en blanco y devuelve su id
    void borrar(int id); //Elimina el elemento con el id indicado
    int tamanyo(); //Devuelve el número de elementos
    void actualiza(int id, Cliente cliente); //Reemplaza un elemento
    void eliminaListaClientes();
}
