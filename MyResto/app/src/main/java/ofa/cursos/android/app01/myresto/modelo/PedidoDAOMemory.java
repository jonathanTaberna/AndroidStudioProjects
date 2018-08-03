package ofa.cursos.android.app01.myresto.modelo;

import java.util.ArrayList;
import java.util.List;

public class PedidoDAOMemory implements PedidoDao {
    private static List<Pedido> REPOSITORIO_PEDIDOS = new ArrayList<>();
    @Override
    public void agregar(Pedido pedido) {
        REPOSITORIO_PEDIDOS.add(pedido);
    }

    @Override
    public List<Pedido> listarTodos() {
        return REPOSITORIO_PEDIDOS;
    }

    @Override
    public void eliminar(Pedido pedido) {
        REPOSITORIO_PEDIDOS.remove(pedido);
    }

}
