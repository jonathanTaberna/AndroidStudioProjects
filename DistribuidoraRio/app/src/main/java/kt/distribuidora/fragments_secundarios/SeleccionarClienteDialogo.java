package kt.distribuidora.fragments_secundarios;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kt.distribuidora.Interfaces.Clientes;
import kt.distribuidora.Interfaces.ClientesVector;
import kt.distribuidora.R;
import kt.distribuidora.adaptadores.AdaptadorClientes;
import kt.distribuidora.adaptadores.AdaptadorListaClientes;
import kt.distribuidora.elementos.Cliente;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;

public class SeleccionarClienteDialogo {

    private ClientesVector clientesVector = new ClientesVector();
    private Clientes clientes;
    private AdaptadorListaClientes adaptadorListaClientes;
    private RecyclerView recyclerSeleccionarClienteDialogo;
    private TextView tvSeleccionarClienteDialogoCodigoCliente;
    private Spinner spSeleccionarClienteZona;
    private EditText edtSeleccionarClienteDialogoCliente;
    private Context contexto;
    private RecyclerView.LayoutManager layoutManager;
    boolean flagEligeCliente = false;
    private int zonaSeleccionada = 0;

    private ArrayAdapter<String> arrayAdapter;

    public interface FinalizoSeleccionarClienteDialogo{
        void ResultadoSeleccionarClienteDialogo(long codigoCliente, String nombreCliente);
    }
    private FinalizoSeleccionarClienteDialogo interfaz;

    public SeleccionarClienteDialogo(final Context contexto, final FinalizoSeleccionarClienteDialogo actividad, long codigoCliente, String nombreCliente) {
        this.contexto = contexto;
        interfaz = actividad;

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.seleccionar_cliente_dialogo);

        tvSeleccionarClienteDialogoCodigoCliente = (TextView) dialogo.findViewById(R.id.tvSeleccionarClienteDialogoCodigoCliente);
        spSeleccionarClienteZona = (Spinner) dialogo.findViewById(R.id.spSeleccionarClienteZona);
        edtSeleccionarClienteDialogoCliente = (EditText) dialogo.findViewById(R.id.edtSeleccionarClienteDialogoCliente);
        recyclerSeleccionarClienteDialogo = (RecyclerView) dialogo.findViewById(R.id.recyclerSeleccionarClienteDialogo);

        Button btnSeleccionarClienteDialogoAceptar = (Button) dialogo.findViewById(R.id.btnSeleccionarClienteDialogoAceptar);
        Button btnSeleccionarClienteDialogoCancelar = (Button) dialogo.findViewById(R.id.btnSeleccionarClienteDialogoCancelar);


        llenarSpinerZonas(contexto);

        spSeleccionarClienteZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String zonaS = spSeleccionarClienteZona.getSelectedItem().toString();
                if (zonaS.equals("No hay Zonas cargadas")) {
                    Toast.makeText(contexto, "No hay Zonas cargadas", Toast.LENGTH_LONG).show();
                    return;
                }
                zonaSeleccionada = 0;
                if (!zonaS.isEmpty()) {
                    String[] partesCat = zonaS.split(" - ");
                    zonaSeleccionada = Integer.parseInt(partesCat[0]);
                }
                if (zonaSeleccionada != 0) {
                    llenarRecycler(contexto, "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        TextWatcher textWatcher = new TextWatcher() {
            Context context = contexto;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flagEligeCliente){
                    return;
                }
                String textoIngresado = edtSeleccionarClienteDialogoCliente.getText().toString();
                llenarRecycler(context, textoIngresado);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        flagEligeCliente = false;
        edtSeleccionarClienteDialogoCliente.addTextChangedListener(textWatcher);

        llenarRecycler(contexto, "");

        btnSeleccionarClienteDialogoAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String elemento = edtSeleccionarClienteDialogoCliente.getText().toString();
                if (elemento.isEmpty()){
                    edtSeleccionarClienteDialogoCliente.setError("Debe seleccionar un cliente de la lista para continuar.");
                    return;
                }
                String codigoClienteS = tvSeleccionarClienteDialogoCodigoCliente.getText().toString();
                long codigoCliente = Long.parseLong(codigoClienteS);
                if (codigoCliente == 0){
                    edtSeleccionarClienteDialogoCliente.setError("El nombre seleccionado NO es de un Cliente.");
                    return;
                }
                String zonaS = spSeleccionarClienteZona.getSelectedItem().toString();
                if (zonaS.equals("No hay Zonas cargadas")) {
                    Toast.makeText(contexto, "No hay Zonas cargadas", Toast.LENGTH_LONG).show();
                    return;
                }
                int zona = 0;
                if (!zonaS.isEmpty()) {
                    String[] partesCat = zonaS.split(" - ");
                    zona = Integer.parseInt(partesCat[0]);
                }

                String nombreCliente = edtSeleccionarClienteDialogoCliente.getText().toString();
                interfaz.ResultadoSeleccionarClienteDialogo(codigoCliente, nombreCliente);
                dialogo.dismiss();
            }
        });
        btnSeleccionarClienteDialogoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }

    private void llenarRecycler(Context context, String cadenaDeBusqueda){
        clientesVector.ResetList();

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        String query = "";
        if (cadenaDeBusqueda.isEmpty()) {
            query = "SELECT codigo, nombre FROM clientes WHERE zona = " + zonaSeleccionada;
        } else {
            query = "SELECT codigo, nombre FROM clientes WHERE zona = " + zonaSeleccionada + " AND nombre like '%" + cadenaDeBusqueda + "%'";
        }
        Cursor cursor = db.rawQuery(query, null);

        int cont = 0;
        Cliente cliente = null;

        String elemento = "";
        while (cursor.moveToNext()) {
            cont++;
            int codigo = cursor.getInt(0);
            String nombre = cursor.getString(1);

            cliente = new Cliente(codigo, nombre, 0,0,null,0);
            clientesVector.anyade(cliente);
        }
        cursor.close();

        if (cadenaDeBusqueda.isEmpty()) {
            query = "SELECT dni, nombre FROM clientesNuevos";
        } else {
            query = "SELECT dni, nombre FROM clientesNuevos WHERE nombre like '%" + cadenaDeBusqueda + "%'";
        }
        cursor = db.rawQuery(query, null);
        elemento = "";
        while (cursor.moveToNext()) {
            cont++;
            long dni = cursor.getLong(0);
            String nombre = cursor.getString(1);
            cliente = new Cliente(dni, nombre, 0,0,null,0);
            clientesVector.anyade(cliente);
        }
        cursor.close();

        if (cont == 0) {
            cliente = new Cliente();
            clientesVector.anyade(cliente);
            Toast.makeText(context, "No hay Clientes con el nombre ingresado", Toast.LENGTH_SHORT).show();
            tvSeleccionarClienteDialogoCodigoCliente.setText("" + 0);
            ClientesVector clientesVectorAux = new ClientesVector(clientesVector.getArray(1, 1));
            actualizarVista(clientesVectorAux);
        } else {
            actualizarVista(clientesVector);
        }

    }

    public void actualizarVista(ClientesVector clientesVector){
        clientes = clientesVector;
        adaptadorListaClientes = new AdaptadorListaClientes(contexto, clientes);
        recyclerSeleccionarClienteDialogo.setAdapter(adaptadorListaClientes);
        layoutManager = new LinearLayoutManager(contexto);
        recyclerSeleccionarClienteDialogo.setLayoutManager(layoutManager);
        adaptadorListaClientes.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (int) recyclerSeleccionarClienteDialogo.getChildAdapterPosition(v);
                Cliente cliente = clientes.elemento(id);
                long codigo = cliente.getCodigo();
                String nombre = cliente.getNombre();
                flagEligeCliente = true;
                tvSeleccionarClienteDialogoCodigoCliente.setText("" + codigo);
                edtSeleccionarClienteDialogoCliente.setText(nombre);
                flagEligeCliente = false;
            }
        });
    }


    private void llenarSpinerZonas(Context context) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String query = "SELECT codigo, descripcion FROM zonas";
        Cursor cursor = db.rawQuery(query, null);

        int cont = 0;
        ArrayList<String> arraySpinner = new ArrayList<>();

        String elemento = "";
        while (cursor.moveToNext()) {
            cont++;
            int codigo = cursor.getInt(0);
            String descripcion = cursor.getString(1);

            elemento = codigo + " - " + descripcion;
            arraySpinner.add(elemento);
            if (cont == 1) {
                zonaSeleccionada = codigo;
            }
        }
        cursor.close();
        if (cont == 0) {
            arraySpinner.add("No hay Zonas cargadas");
        }

        arrayAdapter = new ArrayAdapter<String>(contexto,android.R.layout.simple_spinner_item, arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSeleccionarClienteZona.setAdapter(arrayAdapter);
    }
}
