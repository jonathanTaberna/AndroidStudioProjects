package kt.distribuidoraBJ.fragments_datos_cargados;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kt.distribuidoraBJ.MainActivity;
import kt.distribuidoraBJ.R;
import kt.distribuidoraBJ.adaptadores.AdaptadorClientesNuevos;
import kt.distribuidoraBJ.sql.AdminSQLiteOpenHelper;
import kt.distribuidoraBJ.elementos.ClienteNuevo;

public class ClientesNuevosCargadosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerClientesNuevosCargados;
    private ArrayList<ClienteNuevo> listaClientesNuevos;

    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    public ClientesNuevosCargadosFragment() {
        // Required empty public constructor
    }

    public static ClientesNuevosCargadosFragment newInstance(String param1, String param2) {
        ClientesNuevosCargadosFragment fragment = new ClientesNuevosCargadosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View vista = inflater.inflate(R.layout.fragment_clientes_nuevos_cargados, container, false);
        recyclerClientesNuevosCargados= (RecyclerView) vista.findViewById(R.id.recyclerClientesNuevosCargados);

        listaClientesNuevos = new ArrayList<>();
        recyclerClientesNuevosCargados.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        AdaptadorClientesNuevos adaptadorClientesNuevos = new AdaptadorClientesNuevos(listaClientesNuevos);
        recyclerClientesNuevosCargados.setAdapter(adaptadorClientesNuevos);

        adaptadorClientesNuevos.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v)
            {
                int id = (int) recyclerClientesNuevosCargados.getChildAdapterPosition(v);
                ClienteNuevo clienteNuevo = listaClientesNuevos.get(id);
                ((MainActivity) getActivity()).ResultadoNuevoCliente(clienteNuevo);
                return true;
            }
        });
        /*
        adaptadorClientesNuevos.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) recyclerView.getChildAdapterPosition(v);
                Producto prod = productos.elemento(id);
                //cambiar esta llamada, por:
                if (prod.getCodigo() == ""){
                    return;
                }
                if (fragmentVisible == "canje"){
                    new PopUpProductoDialogo(contexto, prod.getCodigo(),prod.getNombre(),prod.getPuntos(), "","C");
                }
                if (fragmentVisible == "promociones"){
                    new PopUpProductoDialogo(contexto, prod.getCodigo(),prod.getNombre(),prod.getPuntos(), prod.getComentario(),"P");
                }
                //new PopUpProductoDialogo(contexto, prod.getFoto1(),prod.getNombre(),prod.getPuntos());

            }
        });
        */
        return vista;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
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

        ClienteNuevo clienteNuevo = null;
        String query = "SELECT dni, categoria, nombre, correo, telefono, direccion, codpos, localidad, zona " +
                        "FROM clientesNuevos";
        Cursor cursor = db.rawQuery(query, null);
        int cont = 0;

        while (cursor.moveToNext()) {
            clienteNuevo = new ClienteNuevo();
            clienteNuevo.setDni(cursor.getLong(0));
            clienteNuevo.setCategoria(cursor.getInt(1));
            clienteNuevo.setNombre(cursor.getString(2));
            clienteNuevo.setCorreo(cursor.getString(3));
            clienteNuevo.setTelefono(cursor.getString(4));
            clienteNuevo.setDireccion(cursor.getString(5));
            clienteNuevo.setCodpos(cursor.getInt(6));
            clienteNuevo.setLocalidad(cursor.getString(7));
            clienteNuevo.setZona(cursor.getInt(8));
            //clienteNuevo.setFecnac(cursor.getInt(8));
            listaClientesNuevos.add(clienteNuevo);
            cont++;
        }

        cursor.close();
        if (cont == 0) {
            //Toast.makeText(getContext(), "No hay Clientes Cargados", Toast.LENGTH_SHORT).show();
            clienteNuevo = new ClienteNuevo();
            listaClientesNuevos.add(clienteNuevo);
        }
    }
}
