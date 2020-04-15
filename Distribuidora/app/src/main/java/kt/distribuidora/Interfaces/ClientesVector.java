package kt.distribuidora.Interfaces;

import java.util.LinkedList;
import java.util.List;

import kt.distribuidora.elementos.Cliente;

public class ClientesVector implements Clientes {
    protected List<Cliente> vectorClientes;

    public ClientesVector() {
        vectorClientes = new LinkedList<>();
    }

    public ClientesVector(List<Cliente> lista) {
        vectorClientes = lista;
    }

    public List<Cliente> getArray(int from, int to){
        return vectorClientes.subList(from - 1,to);
    }

    public void ResetList() {
        vectorClientes.clear();
    }


    public Cliente elemento(int id) {
        return vectorClientes.get(id);
    }

    public void anyade(Cliente cliente) {
        vectorClientes.add(cliente);
    }

    public int nuevo() {
        Cliente cliente = new Cliente();
        vectorClientes.add(cliente);
        return vectorClientes.size()-1;
    }

    public void borrar(int id) {
        vectorClientes.remove(id);
    }

    public int tamanyo() {
        return vectorClientes.size();
    }

    public void actualiza(int id, Cliente cliente) {
        vectorClientes.set(id, cliente);
    }

    public void eliminaListaClientes() {
        vectorClientes.clear(); vectorClientes = null;
    }

}
