package ofa.cursos.android.app01.myresto.modelo;

import java.util.List;

public interface PedidoDao {
    public void agregar(Pedido pedido);
    public void eliminar(Pedido pedido);
    public List<Pedido> listarTodos();
    public Pedido buscarPorId(Integer id);
}
