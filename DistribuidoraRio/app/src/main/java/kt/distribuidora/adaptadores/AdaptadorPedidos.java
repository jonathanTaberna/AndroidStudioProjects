package kt.distribuidora.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kt.distribuidora.R;
import kt.distribuidora.elementos.Articulo;
import kt.distribuidora.elementos.Pedido;

//import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorPedidos extends RecyclerView.Adapter<AdaptadorPedidos.ClienteViewHolder> {

    Context context;
    ArrayList<Pedido> listaPedidos;
    protected View.OnClickListener onClickListener; //Listener para cada elemento
    protected View.OnLongClickListener onLongClickListener; //Listener largo para cada elemento

    public AdaptadorPedidos(ArrayList<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_pedidos, null, false);
        vista.setOnClickListener(onClickListener);
        vista.setOnLongClickListener(onLongClickListener);
        return new ClienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder( ClienteViewHolder holder, int position) {

        try {

            int idPedido = listaPedidos.get(position).getIdPedido();
            long codigoVendedor = listaPedidos.get(position).getCodigoVendedor();
            String nombreVendedor = listaPedidos.get(position).getNombreVendedor();
            long codigoCliente = listaPedidos.get(position).getCodigoCliente();
            String nombreCliente = listaPedidos.get(position).getNombreCliente();
            int listaPrecios = listaPedidos.get(position).getListaPrecios();
            int fechaPedido = listaPedidos.get(position).getFechaPedido();
            String comentariosPedido = listaPedidos.get(position).getComentariosPedido();
            ArrayList<Articulo> listaArticulos = listaPedidos.get(position).getListaArticulos();

            if (idPedido == 0) {
                holder.tvPedidosCodigo.setText("No hay Pedidos para mostrar en este Listado");
                holder.tvPedidosCodigoVendedorNombre.setText("");
                holder.tvPedidosCodigoClienteNombre.setText("");
                holder.tvPedidosListaPrecios.setText("");
                holder.tvPedidosFecha.setText("");
                holder.tvPedidosComentarios.setText("");
                holder.tvPedidosArticulos.setText("");
            } else {
                holder.tvPedidosCodigo.setText("Código: " + idPedido);
                holder.tvPedidosCodigoVendedorNombre.setText("Vendedor: " + codigoVendedor + " - " + nombreVendedor);
                holder.tvPedidosCodigoClienteNombre.setText("Cliente: " + codigoCliente + " - " + nombreCliente);
                holder.tvPedidosListaPrecios.setText("Lista de Precios: " + listaPrecios);
                String fechaPedidoS = "" + fechaPedido;
                String fechaPedidoSS = fechaPedidoS.substring(6, 8) + "/" + fechaPedidoS.substring(4, 6) + "/" + fechaPedidoS.substring(0, 4);
                holder.tvPedidosFecha.setText("Fecha del Pedido: " + fechaPedidoSS);
                holder.tvPedidosComentarios.setText("Comentarios: " + comentariosPedido);
                holder.tvPedidosArticulos.setText("Artículos: ");

                int cantidadArticulos = listaArticulos.size();
                int i = 0;

                if (cantidadArticulos <= 0) {
                    return;
                }
                while (i < cantidadArticulos) {
                    Articulo articulo = listaArticulos.get(i);

                    int codigo = articulo.getCodigo();
                    String descripcion = articulo.getDescripcion();
                    double costo = articulo.getCosto();
                    int codigoLista = articulo.getCodigoLista();
                    double precio = articulo.getPrecio();
                    int cantidad = articulo.getCantidad();
                    int cantidadBonif = articulo.getCantidadBonif();

                    View child = LayoutInflater.from(context).inflate(R.layout.layout_pedidos_articulos, null, false);
                    child.setId(i);

                    TextView tvPedidosArticulosNombreArticulo = (TextView) child.findViewById(R.id.tvPedidosArticulosNombreArticulo);
                    TextView tvPedidosArticulosPrecio = (TextView) child.findViewById(R.id.tvPedidosArticulosPrecio);
                    TextView tvPedidosArticulosCantidadArticulo = (TextView) child.findViewById(R.id.tvPedidosArticulosCantidadArticulo);

                    String precioS = "   $ " + precio;
                    String cantidadS = "   ("+ cantidad + ")";
                    if (cantidadBonif > 0) {
                        cantidadS += "  (-" + cantidadBonif + ")";
                    }

                    tvPedidosArticulosNombreArticulo.setText(descripcion);
                    tvPedidosArticulosPrecio.setText(precioS);
                    tvPedidosArticulosCantidadArticulo.setText(cantidadS);

                    holder.llPedidosArticulos.addView(child);
                    i++;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        public TextView tvPedidosCodigo, tvPedidosCodigoVendedorNombre, tvPedidosCodigoClienteNombre, tvPedidosListaPrecios, tvPedidosFecha, tvPedidosComentarios, tvPedidosArticulos;
        public LinearLayout llPedidosArticulos;

        public ClienteViewHolder( View itemView) {
            super(itemView);

            tvPedidosCodigo = (TextView) itemView.findViewById(R.id.tvPedidosCodigo);
            tvPedidosCodigoVendedorNombre = (TextView) itemView.findViewById(R.id.tvPedidosCodigoVendedorNombre);
            tvPedidosCodigoClienteNombre = (TextView) itemView.findViewById(R.id.tvPedidosCodigoClienteNombre);
            tvPedidosListaPrecios = (TextView) itemView.findViewById(R.id.tvPedidosListaPrecios);
            tvPedidosFecha = (TextView) itemView.findViewById(R.id.tvPedidosFecha);
            tvPedidosComentarios = (TextView) itemView.findViewById(R.id.tvPedidosComentarios);
            tvPedidosArticulos = (TextView) itemView.findViewById(R.id.tvPedidosArticulos);
            llPedidosArticulos = (LinearLayout) itemView.findViewById(R.id.llPedidosArticulos);

        }
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
