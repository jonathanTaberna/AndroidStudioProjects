package kt.distribuidoraBJ.fragments_secundarios;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kt.distribuidoraBJ.R;
import kt.distribuidoraBJ.sql.AdminSQLiteOpenHelper;
import kt.distribuidoraBJ.elementos.Pedido;

public class NuevoPedidoFragment extends Fragment {
    private Context contexto;

    //private Spinner spNuevoCliente;
    private Spinner spPedidoNuevoListaPrecios;
    private TextView tvPedidoNuevoVendedor;
    private TextView tvPedidoNuevoCodigoNombreCliente;
    private EditText edtPedidoNuevoFecha;
    private TextView tvPedidoNuevoComentario;
    private TextView tvPedidoNuevoTotal;
    private EditText edtFiltrarProductos;
    private ScrollView scrollViewPedidoNuevo;
    private LinearLayout ll1PedidoNuevo;
    private Button btnPedidoNuevoEliminar;
    private Button btnPedidoNuevoGuardar;
    private Button btnPedidoNuevoCancelar;

    private int sYear;
    private int sMonth;
    private int sDay;

    private int cont;
    private double totalPedido;
    //private boolean flagEdit;
    private boolean flagGuardadoCorrecto;
    private String comentario;

    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;
    private ArrayAdapter<String> arrayAdapter;

    private Boolean editarInfo;
    private int idPedido;
    private long codigoVendedor;
    private String nombreVendedor;
    private long codigoCliente;
    private String nombreCliente;
    private int listaPrecios;
    private int fechaPedido;
    private String comentariosPedido;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NuevoPedidoFragment() {
        // Required empty public constructor
    }

    public static NuevoPedidoFragment newInstance(boolean editarInfo, Pedido pedido, long codigoCliente, String nombreCliente) {
        NuevoPedidoFragment nuevoPedidoFragment = new NuevoPedidoFragment();
        Bundle args = new Bundle();
        args.putBoolean("editarInfo", editarInfo);
        if (editarInfo) {
            args.putInt("idPedido", pedido.getIdPedido());
            args.putLong("codigoVendedor", pedido.getCodigoVendedor());
            args.putString("nombreVendedor", pedido.getNombreVendedor());
            args.putLong("codigoCliente", pedido.getCodigoCliente());
            args.putString("nombreCliente", pedido.getNombreCliente());
            args.putInt("listaPrecios", pedido.getListaPrecios());
            args.putInt("fechaPedido", pedido.getFechaPedido());
            args.putString("comentariosPedido", pedido.getComentariosPedido());
        } else {
            if (codigoCliente != 0) {
                args.putLong("codigoCliente", codigoCliente);
                args.putString("nombreCliente", nombreCliente);
            }
        }
        nuevoPedidoFragment.setArguments(args);
        return nuevoPedidoFragment;
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
        // Inflate the layout for this fragment
        contexto = container.getContext();
        //return inflater.inflate(R.layout.fragment_nuevo_pedido, container, false);

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        return localInflater.inflate(R.layout.fragment_nuevo_pedido, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        try {
            adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
            adminSQLiteOpenHelper.borrarRegistros("temporalProductos", db);
            adminSQLiteOpenHelper.crearTabla("temporalProductos", db);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);

        final Calendar c = Calendar.getInstance();
        sYear = c.get(Calendar.YEAR);
        sMonth = c.get(Calendar.MONTH);
        sMonth++;
        sDay = c.get(Calendar.DAY_OF_MONTH);

        //spNuevoCliente = (Spinner) view.findViewById(R.id.spNuevoCliente);
        spPedidoNuevoListaPrecios = (Spinner) view.findViewById(R.id.spPedidoNuevoListaPrecios);
        tvPedidoNuevoVendedor = (TextView) view.findViewById(R.id.tvPedidoNuevoVendedor);
        tvPedidoNuevoCodigoNombreCliente = (TextView) view.findViewById(R.id.tvPedidoNuevoCodigoNombreCliente);
        edtPedidoNuevoFecha = (EditText) view.findViewById(R.id.edtPedidoNuevoFecha);
        tvPedidoNuevoComentario = (TextView) view.findViewById(R.id.tvPedidoNuevoComentario);
        tvPedidoNuevoTotal = (TextView) view.findViewById(R.id.tvPedidoNuevoTotal);
        edtFiltrarProductos = (EditText) view.findViewById(R.id.edtFiltrarProductos);
        scrollViewPedidoNuevo = (ScrollView) view.findViewById(R.id.scrollViewPedidoNuevo);
        ll1PedidoNuevo = (LinearLayout) view.findViewById(R.id.ll1PedidoNuevo);

        leerDatosVendedor();
        tvPedidoNuevoVendedor.setText(nombreVendedor);

        btnPedidoNuevoEliminar = (Button) view.findViewById(R.id.btnPedidoNuevoEliminar);
        btnPedidoNuevoGuardar = (Button) view.findViewById(R.id.btnPedidoNuevoGuardar);
        btnPedidoNuevoCancelar = (Button) view.findViewById(R.id.btnPedidoNuevoCancelar);


        TextWatcher textWatcher = new TextWatcher() {
            View vista = view;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                limpiarProductos(ll1PedidoNuevo);
                cont = 0;
                totalPedido = 0;
                llenarArticulos(ll1PedidoNuevo);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        edtFiltrarProductos.addTextChangedListener(textWatcher);

        btnPedidoNuevoGuardar.setOnClickListener(new View.OnClickListener() {
            View vista = view;
            @Override
            public void onClick(View view) {
                flagGuardadoCorrecto = false;
                if (guardarPedido(vista)) { //el guardado fue correcto
                    if (flagGuardadoCorrecto = true) {
                        Toast.makeText(contexto, "Guardado exitoso", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                        return;
                        /*
                        if (editarInfo) {
                            getActivity().onBackPressed();
                            return;
                        } else {
                            reestablecerCampos(vista);
                        }
                        */
                    }
                }
            }
        });

        btnPedidoNuevoCancelar.setOnClickListener(new View.OnClickListener() {
            View vista = view;
            @Override
            public void onClick(View view) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                // Set a title for alert dialog
                builder.setTitle("Cancelar Pedido.");
                builder.setCancelable(false);

                // Ask the final question
                builder.setMessage("Desea cancelar el pedido actual?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button

                        if (editarInfo){
                            getActivity().onBackPressed();
                            return;
                        } else {
                            reestablecerCampos(vista);
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


            }
        });

        btnPedidoNuevoEliminar.setVisibility(View.INVISIBLE);
        btnPedidoNuevoEliminar.setOnClickListener(new View.OnClickListener() {
            View vista = view;
            @Override
            public void onClick(View view) {

                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                // Set a title for alert dialog
                builder.setTitle("Eliminar Pedido.");
                builder.setCancelable(false);

                // Ask the final question
                builder.setMessage("Desea eliminar el pedido?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button

                        eliminarPedido();
                        //reestablecerCampos(vista);
                        Toast.makeText(contexto, "Pedido eliminado CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                        return;
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


            }
        });

        edtPedidoNuevoFecha.setText(generarFecha(sDay,sMonth,sYear));
        edtPedidoNuevoFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        tvPedidoNuevoComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpEditText(tvPedidoNuevoComentario.getText().toString());
            }
        });

        cont = 0;
        totalPedido = 0;
        tvPedidoNuevoTotal.setText("$ " + totalPedido);

        editarInfo = getArguments().getBoolean("editarInfo");
        if (editarInfo) {
            btnPedidoNuevoEliminar.setVisibility(View.VISIBLE);
            idPedido = getArguments().getInt("idPedido");
            codigoCliente = getArguments().getLong("codigoCliente");
            nombreCliente = getArguments().getString("nombreCliente");
            listaPrecios = getArguments().getInt("listaPrecios");
            fechaPedido = getArguments().getInt("fechaPedido");
            comentariosPedido = getArguments().getString("comentariosPedido");

            setearDatos(codigoCliente, nombreCliente, fechaPedido, comentariosPedido);
        } else {
            codigoCliente = getArguments().getLong("codigoCliente");
            nombreCliente = getArguments().getString("nombreCliente");
            String elemento = codigoCliente + " - " + nombreCliente;
            tvPedidoNuevoCodigoNombreCliente.setText(elemento);
        }

        llenarSpinerListaPrecios(view);
        spPedidoNuevoListaPrecios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                try {
                    String elemento = spPedidoNuevoListaPrecios.getSelectedItem().toString();
                    String[] partes = elemento.split(" - ");
                    int listaPrecios = Integer.parseInt(partes[0]);

                    adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
                    SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
                    //adminSQLiteOpenHelper.borrarRegistros("temporalProductos", db);
                    //adminSQLiteOpenHelper.crearTabla("temporalProductos", db);
                    String query = " INSERT INTO temporalProductos" +
                            " SELECT codigo, 0, 0" +
                            " FROM articulos" +
                            " WHERE codigoLista = " + listaPrecios;

                    db.execSQL(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                limpiarProductos(ll1PedidoNuevo);

                cont = 0;
                totalPedido = 0;
                llenarArticulos(ll1PedidoNuevo);
                tvPedidoNuevoTotal.setText("$ " + totalPedido);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        limpiarProductos(ll1PedidoNuevo);
        cont = 0;
        totalPedido = 0;
        llenarArticulos(ll1PedidoNuevo);
        tvPedidoNuevoTotal.setText("$ " + totalPedido);

    }

    private void setearDatos(long codigoCliente, String  nombreCliente, int fechaPedido, String comentariosPedido){
        tvPedidoNuevoVendedor.setText(nombreVendedor);

        spPedidoNuevoListaPrecios.setSelection(0);

        String elemento = codigoCliente + " - " + nombreCliente;
        tvPedidoNuevoCodigoNombreCliente.setText(elemento);

        String fechaPedidoS = "" + fechaPedido;
        String fechaPedidoSS = fechaPedidoS.substring(6, 8) + "/" + fechaPedidoS.substring(4, 6) + "/" + fechaPedidoS.substring(0, 4);
        edtPedidoNuevoFecha.setText(fechaPedidoSS);
        tvPedidoNuevoComentario.setText(comentariosPedido);

    }

    private void llenarSpinerListaPrecios(View view) {
        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        String query = "SELECT codigo, descripcion FROM categorias";
        Cursor cursor = db.rawQuery(query, null);

        int cont = 0;
        ArrayList<String> arraySpinner = new ArrayList<>();

        String elemento = "";
        while (cursor.moveToNext()) {
            cont++;
            int codigoLista = cursor.getInt(0);
            String nombreLista = cursor.getString(1);
            elemento = codigoLista + " - " + nombreLista;
            arraySpinner.add(elemento);
        }
        cursor.close();

        if (cont == 0) {
            arraySpinner.add("No hay Listas de Precios cargadas");
        }

        arrayAdapter = new ArrayAdapter<String>(contexto,android.R.layout.simple_spinner_item, arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPedidoNuevoListaPrecios.setAdapter(arrayAdapter);

        String cliente = tvPedidoNuevoCodigoNombreCliente.getText().toString();
        if (cliente.equals("No hay Clientes cargados")) {
            return;
        }
        String[] partes = cliente.split(" - ");
        long codigoCliente = Long.parseLong(partes[0]);

        if (editarInfo){
            query = "SELECT descripcion FROM categorias WHERE codigo = " + this.listaPrecios;
            cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                String nombreLista = cursor.getString(0);
                elemento = this.listaPrecios + " - " + nombreLista;
                spPedidoNuevoListaPrecios.setSelection(arrayAdapter.getPosition(elemento));
            } else {
                spPedidoNuevoListaPrecios.setSelection(0);
            }
        } else {
            query = "SELECT codigoLista FROM clientes WHERE codigo = " + codigoCliente;
            cursor = db.rawQuery(query, null);
            int codigoLista = 0;
            if (cursor.moveToFirst()) {
                codigoLista = cursor.getInt(0);
                if (codigoLista != 0) {
                    query = "SELECT descripcion FROM categorias WHERE codigo = " + codigoLista;
                    cursor = db.rawQuery(query, null);
                    if (cursor.moveToFirst()){
                        String nombreLista = cursor.getString(0);
                        elemento = codigoLista + " - " + nombreLista;
                        spPedidoNuevoListaPrecios.setSelection(arrayAdapter.getPosition(elemento));
                    } else {
                        spPedidoNuevoListaPrecios.setSelection(0);
                    }
                } else {
                    spPedidoNuevoListaPrecios.setSelection(0);
                }
            } else {
                cursor.close();
                query = "SELECT categoria FROM clientesNuevos WHERE dni = " + codigoCliente;
                cursor = db.rawQuery(query, null);
                codigoLista = 0;
                if (cursor.moveToFirst()) {
                    codigoLista = cursor.getInt(0);
                    if (codigoLista != 0) {
                        query = "SELECT descripcion FROM categorias WHERE codigo = " + codigoLista;
                        cursor = db.rawQuery(query, null);
                        if (cursor.moveToFirst()){
                            String nombreLista = cursor.getString(0);
                            elemento = codigoLista + " - " + nombreLista;
                            spPedidoNuevoListaPrecios.setSelection(arrayAdapter.getPosition(elemento));
                        } else {
                            spPedidoNuevoListaPrecios.setSelection(0);
                        }
                    } else {
                        spPedidoNuevoListaPrecios.setSelection(0);
                    }
                } else {
                    spPedidoNuevoListaPrecios.setSelection(0);
                }
            }
            cursor.close();
        }
    }

    private void llenarArticulos(LinearLayout linearLayout) {

        String cliente = tvPedidoNuevoCodigoNombreCliente.getText().toString();
        if (cliente.equals("No hay Clientes cargados")) {
            Toast.makeText(contexto, "No hay Clientes cargados.", Toast.LENGTH_LONG).show();
            return;
        }
        String[] partes = cliente.split(" - ");
        long codigoCliente = Long.parseLong(partes[0]);

        String elemento = spPedidoNuevoListaPrecios.getSelectedItem().toString();
        partes = elemento.split(" - ");
        int listaPrecios = Integer.parseInt(partes[0]);

//        limpiarProductos(linearLayout);

        String filtro = edtFiltrarProductos.getText().toString();
        String query = "";
        if (filtro.isEmpty()) {
            query = "SELECT A.codigo," +
                    " A.descripcion," +
                    " A.costo," +
                    " A.precio," +
                    " CASE WHEN TP.cantidadProducto <> 0 THEN TP.cantidadProducto ELSE PP.cantidadProducto END," +
                    " CASE WHEN TP.cantidadProductoBonif <> 0 THEN TP.cantidadProductoBonif ELSE PP.cantidadProductoBonif END" +
                    " FROM articulos AS A " +
                    " LEFT JOIN productosPedidos AS PP " +
                    " ON (A.codigo = PP.codigoProducto AND PP.idPedido = " + idPedido + ") " +
                    " LEFT JOIN temporalProductos AS TP " +
                    " ON A.codigo = TP.codigoProducto " +
                    " WHERE codigoLista = " + listaPrecios;
        } else {
            query = "SELECT A.codigo," +
                    " A.descripcion," +
                    " A.costo," +
                    " A.precio," +
                    " CASE WHEN TP.cantidadProducto <> 0 THEN TP.cantidadProducto ELSE PP.cantidadProducto END," +
                    " CASE WHEN TP.cantidadProductoBonif <> 0 THEN TP.cantidadProductoBonif ELSE PP.cantidadProductoBonif END" +
                    " FROM articulos AS A " +
                    " LEFT JOIN productosPedidos AS PP " +
                    " ON (A.codigo = PP.codigoProducto AND PP.idPedido = " + idPedido + ") " +
                    " LEFT JOIN temporalProductos AS TP " +
                    " ON A.codigo = TP.codigoProducto " +
                    " WHERE codigoLista = " + listaPrecios +
                      " and A.descripcion like '%" + filtro + "%'";
        }
        cargarArticulos(linearLayout, query, filtro);
    }

    private void cargarArticulos(LinearLayout linearLayout, String query, String filtro) {
        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            cont++;

            int codigo = cursor.getInt(0);
            String nombre = cursor.getString(1);
            double costo = cursor.getDouble(2);
            final double precio = cursor.getDouble(3);
            int cantidadArt = cursor.getInt(4);
            int cantidadArtBonif = cursor.getInt(5);

            View child = getLayoutInflater().inflate(R.layout.layout_articulos, null);
            child.setId(cont);

            final TextView desc = (TextView) child.findViewById(R.id.tvNombreArticulo);
            final TextView tvPrecio = (TextView) child.findViewById(R.id.tvPrecio);

            Button btnMas = (Button) child.findViewById(R.id.btnMas);
            final TextView tvCantidadArticulo = (TextView) child.findViewById(R.id.tvCantidadArticulo);
            Button btnMenos = (Button) child.findViewById(R.id.btnMenos);
            final TextView tvCantidad = (TextView) child.findViewById(R.id.tvCantidad);

            Button btnMasBonif = (Button) child.findViewById(R.id.btnMasBonif);
            final TextView tvCantidadArticuloBonif = (TextView) child.findViewById(R.id.tvCantidadArticuloBonif);
            Button btnMenosBonif = (Button) child.findViewById(R.id.btnMenosBonif);
            final TextView tvCantidadBonif = (TextView) child.findViewById(R.id.tvCantidadBonif);

            final int[] cantidad = {0};
            /*
            if (editarInfo && cantidadArt > 0) {
                tvCantidad.setText("" + cantidadArt);
                cantidad[0] = cantidadArt;
                totalPedido += cantidadArt * precio;
            } else {
                tvCantidad.setText("0");
            }
            */
            tvCantidad.setText("" + cantidadArt);
            cantidad[0] = cantidadArt;
            totalPedido += cantidadArt * precio;

            final int[] cantidadBonif = {0};
            /*
            if (editarInfo && cantidadArtBonif > 0) {
                tvCantidadBonif.setText("" + cantidadArtBonif);
                cantidadBonif[0] = cantidadArtBonif;
                totalPedido -= cantidadArtBonif * precio;
            } else {
                tvCantidadBonif.setText("0");
            }
            */
            tvCantidadBonif.setText("" + cantidadArtBonif);
            cantidadBonif[0] = cantidadArtBonif;
            totalPedido -= cantidadArtBonif * precio;

            tvCantidadArticulo.setText("" + cantidad[0]);
            tvCantidadArticuloBonif.setText("" + cantidadBonif[0]);
            tvPrecio.setText("$ " + precio);
            desc.setText(codigo + " - " + nombre);

            btnMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cantidad[0] = Integer.parseInt(tvCantidad.getText().toString());
                    cantidadBonif[0] = Integer.parseInt(tvCantidadBonif.getText().toString());
                    cantidad[0] ++;
                    tvCantidad.setText("" + cantidad[0]);
                    tvCantidadArticulo.setText("" + cantidad[0]);
                    totalPedido += precio;
                    tvPedidoNuevoTotal.setText("$ " + totalPedido);

                    actualizaTemporal(db, codigo, cantidad[0], cantidadBonif[0], precio);
                }
            });
            btnMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cantidad[0] > 0) {
                        cantidad[0] = Integer.parseInt(tvCantidad.getText().toString());
                        cantidadBonif[0] = Integer.parseInt(tvCantidadBonif.getText().toString());
                        cantidad[0] --;
                        tvCantidad.setText("" + cantidad[0]);
                        tvCantidadArticulo.setText("" + cantidad[0]);
                        totalPedido -= precio;
                        tvPedidoNuevoTotal.setText("$ " + totalPedido);

                        actualizaTemporal(db, codigo, cantidad[0], cantidadBonif[0], precio);
                    }
                }
            });

            tvCantidadArticulo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        // code to execute when EditText loses focus
                        String cantS = tvCantidadArticulo.getText().toString();
                        if (cantS.isEmpty()) {
                            return;
                        }
                        int cant = Integer.parseInt(cantS);
                        if (cantidad[0] > 0) {
                            totalPedido -= precio * cantidad[0];
                        }
                        tvCantidadArticulo.setText("" + cant);
                        totalPedido += precio * cant;
                        tvPedidoNuevoTotal.setText("$ " + totalPedido);
                        cantidad[0] = cant;
                        cantidadBonif[0] = Integer.parseInt(tvCantidadBonif.getText().toString());

                        actualizaTemporal(db, codigo, cantidad[0], cantidadBonif[0], precio);
                    }
                }
            });

            btnMasBonif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cantidadBonif[0] = Integer.parseInt(tvCantidadBonif.getText().toString());
                    cantidad[0] = Integer.parseInt(tvCantidad.getText().toString());

                    if (cantidadBonif[0] + 1 > cantidad[0]) {
                        Toast.makeText(contexto, "No se pueden asignar más bonificaciones a este Artículo.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    cantidadBonif[0] ++;
                    tvCantidadBonif.setText("" + cantidadBonif[0]);
                    tvCantidadArticuloBonif.setText("" + cantidadBonif[0]);
                    totalPedido -= precio;
                    tvPedidoNuevoTotal.setText("$ " + totalPedido);
                    cantidad[0] = Integer.parseInt(tvCantidad.getText().toString());

                    actualizaTemporal(db, codigo, cantidad[0], cantidadBonif[0], precio);
                }
            });
            btnMenosBonif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cantidadBonif[0] > 0) {
                        cantidadBonif[0] = Integer.parseInt(tvCantidadBonif.getText().toString());
                        cantidadBonif[0] --;
                        tvCantidadBonif.setText("" + cantidadBonif[0]);
                        tvCantidadArticuloBonif.setText("" + cantidadBonif[0]);
                        totalPedido += precio;
                        tvPedidoNuevoTotal.setText("$ " + totalPedido);
                        cantidad[0] = Integer.parseInt(tvCantidad.getText().toString());

                        actualizaTemporal(db, codigo, cantidad[0], cantidadBonif[0], precio);
                    }
                }
            });

            linearLayout.addView(child);

        }
        if (cont == 0) {
            if (filtro.isEmpty()) {
                Toast.makeText(contexto, "No hay Precios definidos sobre los articulos para la lista de precios: " + spPedidoNuevoListaPrecios.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(contexto, "No hay Productos definidos para el filtro ingresado", Toast.LENGTH_LONG).show();
            }
        }
        cursor.close();

    }

    private void actualizaTemporal(SQLiteDatabase db, int codigo, int cantidadProducto, int cantidadBonifProducto, double precio){
        ContentValues registro = new ContentValues();
        int result = 0;
        registro.clear();
        registro.put("cantidadProducto", cantidadProducto);
        registro.put("cantidadProductoBonif", cantidadBonifProducto);

        result = db.update("temporalProductos", registro, "codigoProducto = " + codigo, null);
        if (result < 0) { //error al regrabar
            Toast.makeText(contexto, "Error al guardar los datos temporales -1-", Toast.LENGTH_SHORT).show();
            db.close();
        }
    }

    private void popUpEditText(String textoCargado) {
        final String[] valor = {""};
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Comentario");
        builder.setCancelable(false);

        final EditText input = new EditText(contexto);
        input.setText(textoCargado);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do something here on OK
                comentario = input.getText().toString();
                tvPedidoNuevoComentario.setText(comentario);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void reestablecerCampos(View vista){
        edtPedidoNuevoFecha.setText(generarFecha(sDay,sMonth,sYear));
        tvPedidoNuevoComentario.setText("");
        if (cont > 0) {
            int i = 1;
            while (i <= cont) {
                View child = (View) vista.findViewById(i) ;
                TextView tvCantidadArticulo = (TextView) child.findViewById(R.id.tvCantidadArticulo);
                final TextView tvCantidad = (TextView) child.findViewById(R.id.tvCantidad);
                final TextView tvPrecio = (TextView) child.findViewById(R.id.tvPrecio);
                TextView tvCantidadArticuloBonif = (TextView) child.findViewById(R.id.tvCantidadArticuloBonif);
                final TextView tvCantidadBonif = (TextView) child.findViewById(R.id.tvCantidadBonif);

                int cantidad = 0;
                int cantidadBonif = 0;
                tvCantidadArticulo.setText("" + cantidad);
                tvCantidad.setText("" + cantidad);
                tvCantidadArticuloBonif.setText("" + cantidadBonif);
                tvCantidadBonif.setText("" + cantidadBonif);
                i++;
            }
        }
        totalPedido = 0;
        tvPedidoNuevoTotal.setText("$ " + totalPedido);
        btnPedidoNuevoEliminar.setVisibility(View.INVISIBLE);
    }

    private void leerDatosVendedor(){
        String retorno = "";

        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        String query = "SELECT codigo, nombre FROM vendedores";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            codigoVendedor = cursor.getLong(0);
            nombreVendedor = cursor.getString(1);
        }
        cursor.close();
    }

    private void eliminarPedido(){
        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
        if (idPedido == 0) {
            return;
        }
        String query = "DELETE FROM pedidos WHERE idPedido = " + idPedido;
        db.execSQL(query);

        query = "DELETE FROM productosPedidos WHERE idPedido = " + idPedido;
        db.execSQL(query);

        db.close();
    }

    private boolean guardarPedido(View vista) {
        boolean retorno = false;
        try {
            adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
            int idPedido = 0;
            //Obtenemos el ultimo ID de pedidos para poder generar uno nuevo
            //String query = "SELECT idPedido " +
            //        "FROM pedidos " +
            //        "ORDER BY idPedido DESC";
            String query = "SELECT ultimoPedidoGuardado " +
                            "FROM vendedores";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                idPedido = cursor.getInt(0);
            }
            idPedido++;
            cursor.close();

            SQLiteDatabase db2 = adminSQLiteOpenHelper.getWritableDatabase();
            adminSQLiteOpenHelper.crearTabla("productosPedidos", db2);
            adminSQLiteOpenHelper.crearTabla("pedidos", db2);

            long codigoCliente = 0;
            String nombreCliente = "";
            int listaPrecios = 0;
            int fechaPedido = 0;
            String comentariosPedido = "";

            nombreVendedor = tvPedidoNuevoVendedor.getText().toString();
            //listaPrecios = Integer.parseInt(spPedidoNuevoListaPrecios.getSelectedItem().toString());
            String elemento = spPedidoNuevoListaPrecios.getSelectedItem().toString();
            String[] partes = elemento.split(" - ");
            listaPrecios = Integer.parseInt(partes[0]);

            //String cliente = spNuevoCliente.getSelectedItem().toString();
            String cliente = tvPedidoNuevoCodigoNombreCliente.getText().toString();
            if (cliente.equals("No hay Clientes cargados")) {
                Toast.makeText(contexto, "No hay Clientes cargados.", Toast.LENGTH_LONG).show();
                return retorno;
            }
            partes = cliente.split(" - ");
            codigoCliente = Long.parseLong(partes[0]);
            nombreCliente = partes[1];
            String fechaPedidoS = edtPedidoNuevoFecha.getText().toString();
            partes = fechaPedidoS.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            String diaS;
            if (dia < 10) {
                diaS = "0" + dia;
            } else {
                diaS = "" + dia;
            }
            String mesS;
            if (mes < 10) {
                mesS = "0" + mes;
            } else {
                mesS = "" + mes;
            }

            fechaPedido = Integer.parseInt("" + ano + mesS + diaS);
            comentariosPedido = tvPedidoNuevoComentario.getText().toString();

            ContentValues registro = new ContentValues();
            int result = 0;
            boolean primerInsert = true;

            int codigoProducto = 0;
            String nombreProducto = "";
            int cantidadProducto = 0;
            int cantidadProductoBonif = 0;
            double precioProducto = 0;

            if (cont > 0) {
                int i = 1;
                while (i <= cont) {
                    View child = (View) vista.findViewById(i);
                    TextView tvNombreArticulo = (TextView) child.findViewById(R.id.tvNombreArticulo);
                    TextView tvCantidadArticulo = (TextView) child.findViewById(R.id.tvCantidadArticulo);
                    final TextView tvCantidad = (TextView) child.findViewById(R.id.tvCantidad);
                    final TextView tvPrecio = (TextView) child.findViewById(R.id.tvPrecio);
                    TextView tvCantidadArticuloBonif = (TextView) child.findViewById(R.id.tvCantidadArticuloBonif);
                    final TextView tvCantidadBonif = (TextView) child.findViewById(R.id.tvCantidadBonif);

                    codigoProducto = 0;
                    nombreProducto = "";
                    cantidadProducto = 0;
                    cantidadProductoBonif = 0;
                    precioProducto = 0;

                    String codigoS = tvNombreArticulo.getText().toString();
                    String[] partes2 = codigoS.split(" - ");
                    codigoProducto = Integer.parseInt(partes2[0]);
                    nombreProducto = partes2[1];
                    String cantidadS = tvCantidadArticulo.getText().toString();
                    if (!cantidadS.isEmpty()) {
                        cantidadProducto = Integer.parseInt(cantidadS);
                    }
                    String cantidadBonifS = tvCantidadArticuloBonif.getText().toString();
                    if (!cantidadBonifS.isEmpty()) {
                        cantidadProductoBonif = Integer.parseInt(cantidadBonifS);
                    }
                    String precioS = tvPrecio.getText().toString();
                    precioS = precioS.substring(2);
                    //precioS = "50.00";
                    System.out.println(Double.parseDouble(precioS));
                    //System.out.println(Double.valueOf(precioS));
                    if (!precioS.isEmpty()) {
                        precioProducto = Double.parseDouble(precioS);
                    }

                    if (cantidadProducto == 0) {
                        i++;
                        continue;
                    }

                    if (editarInfo) {
                        if (primerInsert) {
                            registro.clear();
                            registro.put("idPedido", this.idPedido);
                            registro.put("codigoVendedor", codigoVendedor);
                            registro.put("nombreVendedor", nombreVendedor);
                            registro.put("codigoCliente", codigoCliente);
                            registro.put("nombreCliente", nombreCliente);
                            registro.put("codigoListaPrecios", listaPrecios);
                            registro.put("fechaPedido", fechaPedido);
                            registro.put("comentariosPedido", comentariosPedido);

                            result = db.update("pedidos", registro, "idPedido = " + this.idPedido, null);
                            if (result < 0) { //error al regrabar
                                Toast.makeText(contexto, "Error al guardar los datos del Pedido -3-", Toast.LENGTH_SHORT).show();
                                db.close();
                                flagGuardadoCorrecto = false;
                                return retorno;
                            }
                            result = 0;
                            primerInsert = false;
                            flagGuardadoCorrecto = true;
                        }
                        registro.clear();
                        registro.put("idPedido", this.idPedido);
                        registro.put("codigoProducto", codigoProducto);
                        registro.put("nombreProducto", nombreProducto);
                        registro.put("cantidadProducto", cantidadProducto);
                        registro.put("cantidadProductoBonif", cantidadProductoBonif);
                        registro.put("precioProducto", precioProducto);
                        String whereClause = "idPedido = " + this.idPedido + " AND codigoProducto = " + codigoProducto;

                        result = (int) db.insert("productosPedidos", null, registro);
                        if (result < 0) { //error al insertar
                            result = db.update("productosPedidos", registro, whereClause, null);
                            if (result < 0) { //error al regrabar
                                Toast.makeText(contexto, "Error al guardar los datos del Pedido -4-", Toast.LENGTH_SHORT).show();
                                flagGuardadoCorrecto = false;
                                return retorno;
                            }
                        }
                    } else {
                        if (primerInsert) {
                            registro.clear();
                            registro.put("idPedido", idPedido);
                            registro.put("codigoVendedor", codigoVendedor);
                            registro.put("nombreVendedor", nombreVendedor);
                            registro.put("codigoCliente", codigoCliente);
                            registro.put("nombreCliente", nombreCliente);
                            registro.put("codigoListaPrecios", listaPrecios);
                            registro.put("fechaPedido", fechaPedido);
                            registro.put("comentariosPedido", comentariosPedido);
                            result = (int) db.insert("pedidos", null, registro);
                            if (result < 0) { //error al insertar
                                Toast.makeText(contexto, "Error al guardar los datos del Pedido -1-", Toast.LENGTH_SHORT).show();
                                flagGuardadoCorrecto = false;
                                return retorno;
                            }
                            result = 0;
                            primerInsert = false;
                            flagGuardadoCorrecto = true;

                            registro.clear();
                            registro.put("ultimoPedidoGuardado", idPedido);
                            result = db.update("vendedores", registro, null, null);
                            if (result < 0) { //error al regrabar
                                Toast.makeText(contexto, "Error al guardar los datos del Pedido -5-", Toast.LENGTH_SHORT).show();
                                flagGuardadoCorrecto = false;
                                return retorno;
                            }
                        }

                        registro.clear();
                        registro.put("idPedido", idPedido);
                        registro.put("codigoProducto", codigoProducto);
                        registro.put("nombreProducto", nombreProducto);
                        registro.put("cantidadProducto", cantidadProducto);
                        registro.put("cantidadProductoBonif", cantidadProductoBonif);
                        registro.put("precioProducto", precioProducto);

                        result = (int) db.insert("productosPedidos", null, registro);
                        if (result < 0) { //error al insertar
                            flagGuardadoCorrecto = false;
                            break;
                        }
                    }
                    i++;
                }
            } else {
                retorno = false;
                flagGuardadoCorrecto = false;
                return retorno;
            }
            if (result < 0) { //error al insertar
                Toast.makeText(contexto, "Error al guardar los datos del Pedido -2-", Toast.LENGTH_SHORT).show();
                return retorno;
            }

            retorno = true;
            flagGuardadoCorrecto = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return retorno;
        }
    }

    private void showDatePickerDialog() {

        String [] dma = edtPedidoNuevoFecha.getText().toString().split("/");

        int anio = Integer.parseInt(dma[2]);
        int mes = Integer.parseInt(dma[1]) - 1; //los meses comienzan desde 0
        int dia = Integer.parseInt(dma[0]);

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                String selectedDate = generarFecha(day, month+1, year);
                edtPedidoNuevoFecha.setText(selectedDate);
            }
        }, anio, mes, dia);
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private String generarFecha(int dia, int mes, int anio){
        String fecha = "";

        String diaS;
        if (dia < 10) {
            diaS = "0" + dia;
        } else {
            diaS = "" + dia;
        }
        String mesS;
        if (mes < 10) {
            mesS = "0" + mes;
        } else {
            mesS = "" + mes;
        }

        fecha = diaS + "/" + mesS + "/" + anio;

        return fecha;
    }

    private void limpiarProductos(LinearLayout linearLayout){
        if (cont > 0) {
            int i = 1;
            while (i <= cont) {
                View child = (View) linearLayout.findViewById(i) ;
                ll1PedidoNuevo.removeView(child);
                i++;
            }
        }
    }
}