package kt.distribuidora.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//import androidx.recyclerview.widget.RecyclerView;
import kt.distribuidora.R;
import kt.distribuidora.elementos.Cliente;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ClienteViewHolder> {

    Context context;
    ArrayList<Cliente> listaClientes;

    public AdaptadorClientes(ArrayList<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_clientes, null, false);
        return new ClienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder( ClienteViewHolder holder, int position) {

        try {
            long codigo = listaClientes.get(position).getCodigo();
            String nombre = listaClientes.get(position).getNombre();
            int codigoLista = listaClientes.get(position).getCodigoLista();
            double costo = listaClientes.get(position).getCosto();
            String facturaConLista = "No";
            if (listaClientes.get(position).getFacturaConLista()) {
                facturaConLista = "Si";
            }

            if (codigo == 0) {
                holder.tvCodigoNombre.setText("No hay Clientes cargados");
                holder.tvCodigoListaCosto.setText("");
                holder.tvFacturaConLista.setText("");
            } else {

                holder.tvCodigoNombre.setText("Código: " + codigo + " - " + nombre);
                holder.tvCodigoListaCosto.setText("Código Lista: " + codigoLista + " - Costo: " + costo);
                holder.tvFacturaConLista.setText("Factura con lista?    " + facturaConLista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCodigoNombre, tvCodigoListaCosto, tvFacturaConLista;

        public ClienteViewHolder( View itemView) {
            super(itemView);

            tvCodigoNombre = (TextView) itemView.findViewById(R.id.tvCodigoNombre);
            tvCodigoListaCosto = (TextView)itemView.findViewById(R.id.tvCodigoListaCosto);
            tvFacturaConLista = (TextView) itemView.findViewById(R.id.tvFacturaConLista);

        }
    }
}
