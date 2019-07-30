package hsneoclinica.neoclinica.provisorios;

public interface Turnos {
    Turno elemento(int id); //Devuelve el elemento dado su id
    void anyade(Turno turno); //Añade el elemento indicado
    int nuevo(); //Añade un elemento en blanco y devuelve su id
    void borrar(int id); //Elimina el elemento con el id indicado
    int tamanyo(); //Devuelve el número de elementos
    void actualiza(int id, Turno turno); //Reemplaza un elemento
    void eliminaListaProductos();
}
