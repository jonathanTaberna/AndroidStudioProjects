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

import kt.distribuidoraBJ.R;
import kt.distribuidoraBJ.adaptadores.AdaptadorClientes;
import kt.distribuidoraBJ.sql.AdminSQLiteOpenHelper;
import kt.distribuidoraBJ.elementos.Cliente;

public class ClientesCargadosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerClientesCargados;
    private ArrayList<Cliente> listaClientes;

    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    public ClientesCargadosFragment() {
        // Required empty public constructor
    }
    public static ClientesCargadosFragment newInstance(String param1, String param2) {
        ClientesCargadosFragment fragment = new ClientesCargadosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_clientes_cargados, container, false);
        recyclerClientesCargados = (RecyclerView) vista.findViewById(R.id.recyclerClientesCargados);

        listaClientes = new ArrayList<>();
        recyclerClientesCargados.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        AdaptadorClientes adaptadorClientes = new AdaptadorClientes(listaClientes);
        recyclerClientesCargados.setAdapter(adaptadorClientes);

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
        Cliente cliente = null;
        String query = "SELECT codigo, nombre, codigoLista, costo, facturaConLista, zona FROM clientes";
        Cursor cursor = db.rawQuery(query, null);
        int cont = 0;
        while (cursor.moveToNext()) {
            cliente = new Cliente();
            cliente.setCodigo(cursor.getInt(0));
            cliente.setNombre(cursor.getString(1));
            cliente.setCodigoLista(cursor.getInt(2));
            cliente.setCosto(cursor.getDouble(3));
            cliente.setFacturaConLista(cursor.getInt(4) > 0);
            cliente.setZona(cursor.getInt(5));
            listaClientes.add(cliente);
            cont++;
        }

        cursor.close();
        if (cont == 0) {
            //Toast.makeText(getContext(), "No hay Clientes Cargados", Toast.LENGTH_SHORT).show();
            cliente = new Cliente();
            listaClientes.add(cliente);
        }
    }
}
