package kt.distribuidora.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kt.distribuidora.R;
import kt.distribuidora.elementos.Articulo;

//import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorArticulos extends RecyclerView.Adapter<AdaptadorArticulos.ClienteViewHolder> {

    Context context;
    ArrayList<Articulo> listaArticulos;

    public AdaptadorArticulos(ArrayList<Articulo> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_articulos, null, false);
        return new ClienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder( ClienteViewHolder holder, int position) {

        try {
            int codigo = listaArticulos.get(position).getCodigo();
            String descripcion = listaArticulos.get(position).getDescripcion();
            double costo = listaArticulos.get(position).getCosto();
            int codigoLista = listaArticulos.get(position).getCodigoLista();
            double precio = listaArticulos.get(position).getPrecio();

            if (codigo == 0) {
                holder.tvCodigoDescripcion.setText("No hay Articulos cargados");
                holder.tvCodigoListaCosto.setText("");
                holder.tvPrecio.setText("");
            } else {
                holder.tvCodigoDescripcion.setText("Código: " + codigo + " - " + descripcion);
                holder.tvCodigoListaCosto.setText("Código Lista: " + codigoLista + " - Costo: " + costo);
                holder.tvPrecio.setText("Precio: " + precio);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaArticulos.size();
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCodigoDescripcion, tvCodigoListaCosto, tvPrecio;

        public ClienteViewHolder( View itemView) {
            super(itemView);

            tvCodigoDescripcion = (TextView) itemView.findViewById(R.id.tvCodigoDescripcion);
            tvCodigoListaCosto = (TextView)itemView.findViewById(R.id.tvCodigoListaCosto);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);

        }
    }
}
