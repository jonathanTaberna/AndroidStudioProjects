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
import kt.distribuidoraBJ.adaptadores.AdaptadorVendedor;
import kt.distribuidoraBJ.sql.AdminSQLiteOpenHelper;
import kt.distribuidoraBJ.elementos.Vendedor;

public class VendedoresCargadosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerVendedoresCargados;
    private ArrayList<Vendedor> listaVendedores;

    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    public VendedoresCargadosFragment() {
        // Required empty public constructor
    }
    public static VendedoresCargadosFragment newInstance(String param1, String param2) {
        VendedoresCargadosFragment fragment = new VendedoresCargadosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_vendedores_cargados, container, false);
        recyclerVendedoresCargados = (RecyclerView) vista.findViewById(R.id.recyclerVendedoresCargados);

        listaVendedores = new ArrayList<>();
        recyclerVendedoresCargados.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        AdaptadorVendedor adaptadorVendedor = new AdaptadorVendedor(listaVendedores);
        recyclerVendedoresCargados.setAdapter(adaptadorVendedor);

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
        Vendedor vendedor = null;
        String query = "SELECT codigo, nombre FROM vendedores";
        Cursor cursor = db.rawQuery(query, null);
        int cont = 0;

        while (cursor.moveToNext()) {
            vendedor = new Vendedor();
            vendedor.setCodigo(cursor.getInt(0));
            vendedor.setNombre(cursor.getString(1));
            listaVendedores.add(vendedor);
            cont++;
        }

        cursor.close();
        if (cont == 0) {
            //Toast.makeText(getContext(), "No hay Vendedores Cargados", Toast.LENGTH_SHORT).show();
            vendedor = new Vendedor();
            listaVendedores.add(vendedor);
        }
    }
}
