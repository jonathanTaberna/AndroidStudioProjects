package kt.distribuidora.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

//import androidx.recyclerview.widget.RecyclerView;
import kt.distribuidora.R;
import kt.distribuidora.elementos.Cliente;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ClienteViewHolder> {

    Context context;
    ArrayList<Cliente> listaClientes;

    public AdaptadorClientes(ArrayList<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @Override
    public ClienteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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
            int zona = listaClientes.get(position).getZona();

            AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context, "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

            String query = "SELECT descripcion FROM zonas WHERE codigo = " + zona;
            Cursor cursor = db.rawQuery(query, null);

            String elemento = "";
            String descripcion = "";
            while (cursor.moveToNext()) {
                descripcion = cursor.getString(0);

                elemento = zona + " - " + descripcion;
            }
            cursor.close();

            if (descripcion.isEmpty()){
                elemento = zona + " - " + "No existe numero de zona";
            }

            if (listaClientes.get(position).getFacturaConLista()) {
                facturaConLista = "Si";
            }

            if (codigo == 0) {
                holder.tvCodigoNombre.setText("No hay Clientes cargados");
                holder.tvCodigoListaCosto.setText("");
                holder.tvFacturaConLista.setText("");
                holder.tvZona.setText("");
            } else {

                holder.tvCodigoNombre.setText("Código: " + codigo + " - " + nombre);
                holder.tvCodigoListaCosto.setText("Código Lista: " + codigoLista + " - Costo: " + costo);
                holder.tvFacturaConLista.setText("Factura con lista?    " + facturaConLista);
                holder.tvZona.setText("Zona: " + elemento);
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

        public TextView tvCodigoNombre, tvCodigoListaCosto, tvFacturaConLista, tvZona;

        public ClienteViewHolder( View itemView) {
            super(itemView);

            tvCodigoNombre = (TextView) itemView.findViewById(R.id.tvCodigoNombre);
            tvCodigoListaCosto = (TextView)itemView.findViewById(R.id.tvCodigoListaCosto);
            tvFacturaConLista = (TextView) itemView.findViewById(R.id.tvFacturaConLista);
            tvZona = (TextView) itemView.findViewById(R.id.tvZona);

        }
    }
}
