package kt.distribuidora.elementos;

import java.util.ArrayList;

public class Pedido {

    int idPedido;
    int codigoVendedor;
    String nombreVendedor;
    long codigoCliente;
    String nombreCliente;
    int listaPrecios;
    int fechaPedido;
    String comentariosPedido;
    ArrayList<Articulo> listaArticulos;

    public Pedido() {
        this.idPedido = 0;
        this.codigoVendedor = 0;
        this.nombreVendedor = "";
        this.codigoCliente = 0;
        this.nombreCliente = "";
        this.listaPrecios = 0;
        this.fechaPedido = 0;
        this.comentariosPedido = "";
        this.listaArticulos = new ArrayList<>();
    }

    public Pedido(int idPedido, int codigoVendedor, String nombreVendedor, long codigoCliente, String nombreCliente, int listaPrecios, int fechaPedido, String comentariosPedido, ArrayList<Articulo> listaArticulos) {
        this.idPedido = idPedido;
        this.codigoVendedor = codigoVendedor;
        this.nombreVendedor = nombreVendedor;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
        this.listaPrecios = listaPrecios;
        this.fechaPedido = fechaPedido;
        this.comentariosPedido = comentariosPedido;
        this.listaArticulos = listaArticulos;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getCodigoVendedor() {
        return codigoVendedor;
    }

    public void setCodigoVendedor(int codigoVendedor) {
        this.codigoVendedor = codigoVendedor;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(int listaPrecios) {
        this.listaPrecios = listaPrecios;
    }

    public int getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(int fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getComentariosPedido() {
        return comentariosPedido;
    }

    public void setComentariosPedido(String comentariosPedido) {
        this.comentariosPedido = comentariosPedido;
    }

    public ArrayList<Articulo> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(ArrayList<Articulo> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }
}
