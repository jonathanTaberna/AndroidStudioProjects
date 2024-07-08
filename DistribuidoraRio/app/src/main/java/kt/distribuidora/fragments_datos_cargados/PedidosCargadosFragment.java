package kt.distribuidora.fragments_datos_cargados;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Formatter;

import kt.distribuidora.MainActivity;
import kt.distribuidora.R;
import kt.distribuidora.adaptadores.AdaptadorPedidos;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;
import kt.distribuidora.elementos.Articulo;
import kt.distribuidora.elementos.Pedido;

public class PedidosCargadosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context contexto;

    private String tablaActual;
    private String orden;
    private String orderBy;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerPedidosCargados;
    private ArrayList<Pedido> listaPedidos;

    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    public PedidosCargadosFragment(String tablaActual, String orden, String orderBy) {
        this.tablaActual = tablaActual;
        this.orden = orden;
        this.orderBy = orderBy;
        // Required empty public constructor
    }
/*
    public static PedidosCargadosFragment newInstance(String param1, String param2) {
        PedidosCargadosFragment fragment = new PedidosCargadosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_pedidos_cargados, container, false);

        contexto = container.getContext();

        recyclerPedidosCargados= (RecyclerView) vista.findViewById(R.id.recyclerPedidosCargados);

        listaPedidos = new ArrayList<>();
        recyclerPedidosCargados.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        AdaptadorPedidos adaptadorPedidos = new AdaptadorPedidos(listaPedidos);
        recyclerPedidosCargados.setAdapter(adaptadorPedidos);

        adaptadorPedidos.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v)
            {
                if (tablaActual.equals("pedidosGuardados")) {

                    int id = (int) recyclerPedidosCargados.getChildAdapterPosition(v);
                    final Pedido pedido = listaPedidos.get(id);
                    pedido.getIdPedido();

                    String fechaPedidoS = "" + pedido.getFechaPedido();
                    String fechaPedidoSS = fechaPedidoS.substring(6, 8) + "/" + fechaPedidoS.substring(4, 6) + "/" + fechaPedidoS.substring(0, 4);

                    // Build an AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                    // Set a title for alert dialog
                    builder.setTitle("Restaurar Pedidos.");
                    builder.setCancelable(false);

                    // Ask the final question
                    String pregunta = "Está seguro de querer RESTAURAR el pedido con codigo '" +
                                        pedido.getIdPedido() +
                                        "' del cliente '" +
                                        pedido.getNombreCliente() +
                                        "' del día '" +
                                         fechaPedidoSS +
                                        "'?";
                    builder.setMessage(pregunta);

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (restaurarPedido(pedido)) {
                                Toast.makeText(contexto, "Pedido Restaurado Correctamente", Toast.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            } else {
                                Toast.makeText(contexto, "No se pudo Restaurar el pedido", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface

                    dialog.show();

                    return true;
                } else {
                    int id = (int) recyclerPedidosCargados.getChildAdapterPosition(v);
                    Pedido pedido = listaPedidos.get(id);


                    adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
                    SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
                    String query = "SELECT idPedido, fechaRestauracion FROM pedidosRestaurados WHERE idPedido = " + pedido.getIdPedido();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor.moveToFirst()){
                        Toast.makeText(contexto, "No se puede editar este pedido restaurado", Toast.LENGTH_LONG).show();
                    } else {
                        ((MainActivity) getActivity()).ResultadoPedido(pedido);
                    }

                    return true;
                }
            }
        });
        return vista;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void llenarLista(){

        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        Pedido pedido = null;

        String query = "SELECT idPedido, codigoVendedor, nombreVendedor, codigoCliente, nombreCliente, codigoListaPrecios, fechaPedido, comentariosPedido ";

        if (tablaActual.equals("pedidosGuardados")) {
            query += "FROM pedidosGuardados ";
            switch (this.orden){
                case "codigo":
                    query += "ORDER BY idPedido ";
                    break;
                case "nombre":
                    query += "ORDER BY nombreCliente ";
                    break;
                case "fecha":
                    query += "ORDER BY fechaPedido ";
                    break;
            }
            switch (this.orderBy){
                case "asc":
                    query += "ASC ";
                    break;
                case "desc":
                    query += "DESC ";
                    break;
            }
        } else {
            query += "FROM pedidos";
        }

        Cursor cursor = db.rawQuery(query, null);
        int cont = 0;

        while (cursor.moveToNext()) {
            pedido = new Pedido();

            int idPedido = cursor.getInt(0);
            pedido.setIdPedido(cursor.getInt(0));
            pedido.setCodigoVendedor(cursor.getLong(1));
            pedido.setNombreVendedor(cursor.getString(2));
            pedido.setCodigoCliente(cursor.getLong(3));
            pedido.setNombreCliente(cursor.getString(4));
            pedido.setListaPrecios(cursor.getInt(5));
            pedido.setFechaPedido(cursor.getInt(6));
            pedido.setComentariosPedido(cursor.getString(7));

            ArrayList<Articulo> listaArticulos = new ArrayList<>();
            SQLiteDatabase dbArt = adminSQLiteOpenHelper.getReadableDatabase();
            String queryArt = "SELECT codigoProducto, nombreProducto, cantidadProducto, cantidadProductoBonif, precioProducto ";
            if (tablaActual.equals("pedidosGuardados")) {
                queryArt += "FROM productosPedidosGuardados ";
            } else {
                queryArt += "FROM productosPedidos ";
            }
            queryArt += "WHERE idPedido = " + idPedido ;
            Cursor cursorArt = dbArt.rawQuery(queryArt, null);
            while (cursorArt.moveToNext()) {
                Articulo articulo = new Articulo();
                int codigo = cursorArt.getInt(0);
                String descripcion = cursorArt.getString(1);
                int cantidad = cursorArt.getInt(2);
                int cantidadBonif = cursorArt.getInt(3);
                double precio = cursorArt.getDouble(4);

                articulo.setCodigo(codigo);
                articulo.setDescripcion(descripcion);
                articulo.setCantidad(cantidad);
                articulo.setCantidadBonif(cantidadBonif);
                articulo.setPrecio(precio);
                listaArticulos.add(articulo);
            }
            cursorArt.close();

            pedido.setListaArticulos(listaArticulos);
            listaPedidos.add(pedido);
            cont++;
        }

        cursor.close();
        if (cont == 0) {
            //Toast.makeText(getContext(), "No hay Clientes Cargados", Toast.LENGTH_SHORT).show();
            pedido = new Pedido();
            listaPedidos.add(pedido);
        }
    }

    private boolean restaurarPedido(Pedido pedido){
        boolean retorno = false;
        try {
            adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

            String query = "INSERT INTO pedidos SELECT * FROM pedidosGuardados WHERE idPedido = " + pedido.getIdPedido();
            db.execSQL(query);

            query = "INSERT INTO productosPedidos SELECT * FROM productosPedidosGuardados WHERE idPedido = " + pedido.getIdPedido();
            db.execSQL(query);

            query = "DELETE FROM pedidosGuardados WHERE idPedido = " + pedido.getIdPedido();
            db.execSQL(query);

            query = "DELETE FROM productosPedidosGuardados WHERE idPedido = " + pedido.getIdPedido();
            db.execSQL(query);

            //formatear numero con ceros significativos
            Formatter mesFormater = new Formatter();
            Formatter diaFormater = new Formatter();

            Calendar c = Calendar.getInstance();
            int sYear = c.get(Calendar.YEAR);
            int sMonth = c.get(Calendar.MONTH);
            sMonth++;
            int sDay = c.get(Calendar.DAY_OF_MONTH);

            mesFormater.format("%02d",sMonth);
            diaFormater.format("%02d",sDay);

            int fechaRestauracion = Integer.parseInt(""+ sYear + mesFormater + diaFormater);
            ContentValues registro = new ContentValues();

            registro.put("idPedido", pedido.getIdPedido());
            registro.put("fechaRestauracion", fechaRestauracion);

            long result = db.insert("pedidosRestaurados", null, registro);

            if (result < 0) { //dio error el guardado
                Toast.makeText(contexto, "ERROR al guardar el Respaldo", Toast.LENGTH_SHORT).show();
                retorno = false;
            }

            retorno = true;
        } catch (Exception e){
            retorno = false;
            e.printStackTrace();
        } finally {
            return retorno;
        }

    }
}
