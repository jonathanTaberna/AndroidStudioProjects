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

import kt.distribuidora.R;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;
import kt.distribuidora.elementos.ClienteNuevo;

//import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorClientesNuevos extends RecyclerView.Adapter<AdaptadorClientesNuevos.ClienteNuevoViewHolder> {

    Context contexto;
    ArrayList<ClienteNuevo> listaClientesNuevos;
    protected View.OnClickListener onClickListener; //Listener para cada elemento
    protected View.OnLongClickListener onLongClickListener; //Listener largo para cada elemento

    public AdaptadorClientesNuevos(ArrayList<ClienteNuevo> listaClientesNuevos) {
        this.listaClientesNuevos = listaClientesNuevos;
    }

    @Override
    public ClienteNuevoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        contexto = parent.getContext();
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_clientes_nuevos, null, false);
        vista.setOnClickListener(onClickListener);
        vista.setOnLongClickListener(onLongClickListener);
        return new ClienteNuevoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder( ClienteNuevoViewHolder holder, int position) {

        try {
            long dni = listaClientesNuevos.get(position).getDni();
            int categoria  = listaClientesNuevos.get(position).getCategoria();
            String nombre = listaClientesNuevos.get(position).getNombre();
            String correo = listaClientesNuevos.get(position).getCorreo();
            String telefono = listaClientesNuevos.get(position).getTelefono();
            String direccion = listaClientesNuevos.get(position).getDireccion();
            int codpos = listaClientesNuevos.get(position).getCodpos();
            String localidad = listaClientesNuevos.get(position).getLocalidad();
            /*
            int fecnac = listaClientesNuevos.get(position).getFecnac();
            String fecnacS = "" + fecnac;
            //String fecnacSS = fecnacS.substring(0, 4) + "/" + fecnacS.substring(4, 6) + "/" + fecnacS.substring(6, 8);
            String fecnacSS = "";
            if (fecnac != 0) {
                fecnacSS = fecnacS.substring(6, 8) + "/" + fecnacS.substring(4, 6) + "/" + fecnacS.substring(0, 4);
            }
            */

            if (dni == 0) {
                holder.tvClienteNuevoDni.setText("");
                holder.tvClienteNuevoCategoria.setText("");
                holder.tvClienteNuevoNombre.setText("No hay Clientes Nuevos cargados");
                holder.tvClienteNuevoCorreo.setText("");
                holder.tvClienteNuevoTelefono.setText("");
                holder.tvClienteNuevoDireccion.setText("");
                holder.tvClienteNuevoCodpos.setText("");
                holder.tvClienteNuevoLocalidad.setText("");
                //holder.tvClienteNuevoFechaNac.setText("");

            } else {

                String nombreCategoria = leerCategoria(categoria);

                holder.tvClienteNuevoDni.setText("DNI: " + dni);
                holder.tvClienteNuevoCategoria.setText("Categoria: " + categoria + " - " + nombreCategoria);
                holder.tvClienteNuevoNombre.setText("Nombre: " + nombre);
                holder.tvClienteNuevoCorreo.setText("Correo: " + correo);
                holder.tvClienteNuevoTelefono.setText("Telefono: " + telefono);
                holder.tvClienteNuevoDireccion.setText("Direccion: " + direccion);
                holder.tvClienteNuevoCodpos.setText("Codigo Postal: " + codpos + "     ");
                holder.tvClienteNuevoLocalidad.setText("Localidad: " + localidad);
                //holder.tvClienteNuevoFechaNac.setText("Fecha de Nacimiento: " + fecnacSS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String leerCategoria(int categoria){
        String retorno = "";

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        if (categoria == 0) {
            return retorno;
        }
        Cursor cursor = db.rawQuery("SELECT descripcion FROM categorias WHERE codigo = " + categoria, null);
        if (cursor.moveToFirst()) {
            retorno = cursor.getString(0);
        }
        cursor.close();
        return retorno;
    }

    @Override
    public int getItemCount() {
        return listaClientesNuevos.size();
    }

    public class ClienteNuevoViewHolder extends RecyclerView.ViewHolder {

        public TextView tvClienteNuevoDni, tvClienteNuevoCategoria, tvClienteNuevoNombre, tvClienteNuevoCorreo, tvClienteNuevoTelefono, tvClienteNuevoDireccion;
        public TextView tvClienteNuevoCodpos, tvClienteNuevoLocalidad; //, tvClienteNuevoFechaNac;

        public ClienteNuevoViewHolder( View itemView) {
            super(itemView);

            tvClienteNuevoDni = (TextView) itemView.findViewById(R.id.tvClienteNuevoDni);
            tvClienteNuevoCategoria = (TextView) itemView.findViewById(R.id.tvClienteNuevoCategoria);
            tvClienteNuevoNombre = (TextView)itemView.findViewById(R.id.tvClienteNuevoNombre);
            tvClienteNuevoCorreo = (TextView) itemView.findViewById(R.id.tvClienteNuevoCorreo);
            tvClienteNuevoTelefono = (TextView) itemView.findViewById(R.id.tvClienteNuevoTelefono);
            tvClienteNuevoDireccion = (TextView) itemView.findViewById(R.id.tvClienteNuevoDireccion);
            tvClienteNuevoCodpos = (TextView) itemView.findViewById(R.id.tvClienteNuevoCodpos);
            tvClienteNuevoLocalidad = (TextView) itemView.findViewById(R.id.tvClienteNuevoLocalidad);
            //tvClienteNuevoFechaNac = (TextView) itemView.findViewById(R.id.tvClienteNuevoFechaNac);

        }
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
