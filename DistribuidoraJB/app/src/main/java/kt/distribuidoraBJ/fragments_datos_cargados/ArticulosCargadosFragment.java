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
import kt.distribuidoraBJ.adaptadores.AdaptadorArticulos;
import kt.distribuidoraBJ.sql.AdminSQLiteOpenHelper;
import kt.distribuidoraBJ.elementos.Articulo;

public class ArticulosCargadosFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerArticulosCargados;
    private ArrayList<Articulo> listaArticulo;

    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;

    public ArticulosCargadosFragment() {
        // Required empty public constructor
    }
    public static ArticulosCargadosFragment newInstance(String param1, String param2) {
        ArticulosCargadosFragment fragment = new ArticulosCargadosFragment();
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
        View vista = inflater.inflate(R.layout.fragment_articulos_cargados, container, false);
        recyclerArticulosCargados = (RecyclerView) vista.findViewById(R.id.recyclerArticulosCargados);

        listaArticulo = new ArrayList<>();
        recyclerArticulosCargados.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarLista();

        AdaptadorArticulos adaptadorArticulos= new AdaptadorArticulos(listaArticulo);
        recyclerArticulosCargados.setAdapter(adaptadorArticulos);

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
        Articulo articulo = null;
        String query = "SELECT codigo, descripcion, costo, codigoLista, precio FROM articulos";
        Cursor cursor = db.rawQuery(query, null);
        int cont = 0;

        while (cursor.moveToNext()) {
            articulo = new Articulo();
            articulo.setCodigo(cursor.getInt(0));
            articulo.setDescripcion(cursor.getString(1));
            articulo.setCosto(cursor.getDouble(2));
            articulo.setCodigoLista(cursor.getInt(3));
            articulo.setPrecio(cursor.getDouble(4));
            listaArticulo.add(articulo);
            cont++;
        }

        cursor.close();
        if (cont == 0) {
            //Toast.makeText(getContext(), "No hay Articulos Cargados", Toast.LENGTH_SHORT).show();
            articulo = new Articulo();
            listaArticulo.add(articulo);
        }
    }
}
