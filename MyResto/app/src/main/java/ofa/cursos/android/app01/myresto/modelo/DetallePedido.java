package ofa.cursos.android.app01.myresto.modelo;

public class DetallePedido {

    private int id;
    private ProductoMenu productoPedido;
    private int cantidad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductoMenu getProductoPedido() {
        return productoPedido;
    }

    public void setProductoPedido(ProductoMenu productoPedido) {
        this.productoPedido = productoPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
