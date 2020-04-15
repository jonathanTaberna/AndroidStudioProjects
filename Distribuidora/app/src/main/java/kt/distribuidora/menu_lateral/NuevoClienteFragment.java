package kt.distribuidora.menu_lateral;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.icu.util.Calendar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import kt.distribuidora.R;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;
import kt.distribuidora.elementos.ClienteNuevo;

@SuppressLint("ValidFragment")
public class NuevoClienteFragment  extends Fragment {

    private Context contexto;

    private EditText edtNuevoClienteDni;
    private Spinner spNuevoClienteCategorias;
    private EditText edtNuevoClienteNombre;
    private EditText edtNuevoClienteCorreo;
    private EditText edtNuevoClienteTelefono;
    private EditText edtNuevoClienteDireccion;
    private EditText edtNuevoClienteLocalidad;
    //private EditText edtNuevoClienteFechaNacimiento;
    private EditText edtNuevoClienteCodpos;

    private Button btnNuevoClienteCancelar;
    private Button btnNuevoClienteAceptar;

    private int sYear;
    private int sMonth;
    private int sDay;

    private long dniCargado;
    private Boolean editarInfo;
    private long dni;
    private int categoria;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private String localidad;
    private int codpos;
    //private int fecnac;
    private AdminSQLiteOpenHelper adminSQLiteOpenHelper;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = container.getContext();
        //return inflater.inflate(R.layout.activity_nuevo_cliente, container, false);

        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        return localInflater.inflate(R.layout.activity_nuevo_cliente, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar c = Calendar.getInstance();
        sYear = c.get(Calendar.YEAR);
        sMonth = c.get(Calendar.MONTH);
        sMonth++;
        sDay = c.get(Calendar.DAY_OF_MONTH);

        btnNuevoClienteAceptar = (Button) view.findViewById(R.id.btnNuevoClienteAceptar);
        btnNuevoClienteCancelar = (Button) view.findViewById(R.id.btnNuevoClienteCancelar);

        edtNuevoClienteDni = (EditText) view.findViewById(R.id.edtNuevoClienteDni);
        spNuevoClienteCategorias = (Spinner) view.findViewById(R.id.spNuevoClienteCategorias);
        edtNuevoClienteNombre = (EditText) view.findViewById(R.id.edtNuevoClienteNombre);
        edtNuevoClienteCorreo = (EditText) view.findViewById(R.id.edtNuevoClienteCorreo);
        edtNuevoClienteTelefono = (EditText) view.findViewById(R.id.edtNuevoClienteTelefono);
        edtNuevoClienteDireccion = (EditText) view.findViewById(R.id.edtNuevoClienteDireccion);
        edtNuevoClienteLocalidad = (EditText) view.findViewById(R.id.edtNuevoClienteLocalidad);
        edtNuevoClienteCodpos = (EditText) view.findViewById(R.id.edtNuevoClienteCodpos);
        //edtNuevoClienteFechaNacimiento = (EditText) view.findViewById(R.id.edtNuevoClienteFechaNacimiento);

        llenarSpinerCategorias(view);

        edtNuevoClienteDni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && editarInfo == false) {
                    AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
                    SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

                    String dniS = edtNuevoClienteDni.getText().toString();
                    if (!dniS.isEmpty()) {
                        String query = "SELECT dni, categoria, nombre, correo, telefono, direccion, codpos, localidad " +
                                        "FROM clientesNuevos " +
                                        "WHERE dni = " + dniS;
                        final Cursor cursor = db.rawQuery(query, null);
                        if (cursor.moveToFirst()) {
                            // Build an AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                            // Set a title for alert dialog
                            builder.setTitle("Cliente Existente.");
                            builder.setCancelable(false);

                            // Ask the final question
                            builder.setMessage("El DNI ingresado ya se encuentra cargado. Desea editar la información cargada?");

                            // Set the alert dialog yes button click listener
                            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when user clicked the Yes button

                                    dni = cursor.getLong(0);
                                    categoria = cursor.getInt(1);
                                    nombre = cursor.getString(2);
                                    correo = cursor.getString(3);
                                    telefono = cursor.getString(4);
                                    direccion = cursor.getString(5);
                                    codpos = cursor.getInt(6);
                                    localidad = cursor.getString(7);
                                    //fecnac = cursor.getInt(8);
                                    //setearDatos(dni, categoria, nombre, correo, telefono, direccion, localidad, codpos, fecnac);

                                    setearDatos(dni, categoria, nombre, correo, telefono, direccion, localidad, codpos);
                                }
                            });

                            // Set the alert dialog no button click listener
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do something when No button clicked
                                    limpiarDatos();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            // Display the alert dialog on interface
                            dialog.show();
                        }
                    }
                }
            }
        });


        btnNuevoClienteAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dniS = edtNuevoClienteDni.getText().toString();
                if (dniS.isEmpty()) {
                    edtNuevoClienteDni.setError(getString(R.string.error_field_required));
                    edtNuevoClienteDni.requestFocus();
                    return;
                }
                long dni = Long.parseLong(dniS);
                if (dni <= 0) {
                    //Toast.makeText(contexto, "El DNI debe ser Positivo", Toast.LENGTH_SHORT).show();
                    edtNuevoClienteDni.setError(getString(R.string.error_dni_positivo));
                    edtNuevoClienteDni.requestFocus();
                    return;
                }
                String categoriaS = spNuevoClienteCategorias.getSelectedItem().toString();
                if (categoriaS.equals("No hay Categorias cargadas")) {
                    Toast.makeText(contexto, "No hay Categorias cargadas", Toast.LENGTH_LONG).show();
                    return;
                }
                int categoria = 0;
                if (!categoriaS.isEmpty()) {
                    String[] partesCat = categoriaS.split(" - ");
                    categoria = Integer.parseInt(partesCat[0]);
                }
                String nombre = edtNuevoClienteNombre.getText().toString();
                if (nombre.isEmpty()) {
                    edtNuevoClienteNombre.setError(getString(R.string.error_field_required));
                    edtNuevoClienteNombre.requestFocus();
                    return;
                }

                String correo = edtNuevoClienteCorreo.getText().toString();
                String telefono = edtNuevoClienteTelefono.getText().toString();
                String direccion = edtNuevoClienteDireccion.getText().toString();
                String localidad = edtNuevoClienteLocalidad.getText().toString();
                String codposS = edtNuevoClienteCodpos.getText().toString();
                int codpos = 0;
                if (!codposS.isEmpty()) {
                    codpos = Integer.parseInt(codposS);
                }

                try {
                    ContentValues registro = new ContentValues();

                    registro.put("dni", dni);
                    registro.put("categoria", categoria);
                    registro.put("nombre", nombre);
                    registro.put("correo", correo);
                    registro.put("telefono", telefono);
                    registro.put("direccion", direccion);
                    registro.put("localidad", localidad);
                    registro.put("codpos", codpos);

                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
                    SQLiteDatabase db = admin.getWritableDatabase();
                    int result = 0;
                    if (editarInfo) {
                        result = db.update("clientesNuevos", registro, "dni = " + dniCargado, null);
                        if (result < 0) { //error al regrabar
                            Toast.makeText(contexto, "Error al guardar los datos del Cliente nuevo.", Toast.LENGTH_SHORT).show();
                            edtNuevoClienteDni.requestFocus();
                            db.close();
                            return;
                        }
                    } else {
                        result = (int) db.insert("clientesNuevos", null, registro);
                        if (result < 0) { //error al insertar
                            result = db.update("clientesNuevos", registro, "dni = " + dni, null);
                            if (result < 0) {
                                Toast.makeText(contexto, "Error al guardar los datos del Cliente nuevo.", Toast.LENGTH_SHORT).show();
                                edtNuevoClienteDni.requestFocus();
                                db.close();
                                return;
                            }
                        }
                    }
                    db.close();

                    Toast.makeText(contexto, "Los datos del Cliente nuevo fueron guardados Exitosamente", Toast.LENGTH_LONG).show();

                    limpiarDatos();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnNuevoClienteCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarDatos();
            }
        });

        /*
        edtNuevoClienteFechaNacimiento.setText(generarFecha(sDay,sMonth,sYear));
        edtNuevoClienteFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        */


        editarInfo = getArguments().getBoolean("editarInfo");
        if (editarInfo) {
            dni = getArguments().getLong("dni");
            dniCargado = dni;
            categoria = getArguments().getInt("categoria");
            nombre = getArguments().getString("nombre");
            correo = getArguments().getString("correo");
            telefono = getArguments().getString("telefono");
            direccion = getArguments().getString("direccion");
            localidad = getArguments().getString("localidad");
            codpos = getArguments().getInt("codpos");
            //fecnac = getArguments().getInt("fecnac");
            //setearDatos(dni, categoria, nombre, correo, telefono, direccion, localidad, codpos, fecnac);

            setearDatos(dni, categoria, nombre, correo, telefono, direccion, localidad, codpos);
        }

    }

    private void llenarSpinerCategorias(View view) {

        adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        String query = "SELECT codigo, descripcion FROM categorias";
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
        }
        cursor.close();
        if (cont == 0) {
            arraySpinner.add("No hay Categorias cargadas");
        }

        arrayAdapter = new ArrayAdapter<String>(contexto,android.R.layout.simple_spinner_item, arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNuevoClienteCategorias.setAdapter(arrayAdapter);
    }

    public static NuevoClienteFragment newInstance(boolean editarInfo, ClienteNuevo clienteNuevo) {
        NuevoClienteFragment nuevoClienteFragment = new NuevoClienteFragment();
        Bundle args = new Bundle();
        args.putBoolean("editarInfo", editarInfo);
        if (editarInfo) {
            args.putLong("dni", clienteNuevo.getDni());
            args.putInt("categoria", clienteNuevo.getCategoria());
            args.putString("nombre", clienteNuevo.getNombre());
            args.putString("correo", clienteNuevo.getCorreo());
            args.putString("telefono", clienteNuevo.getTelefono());
            args.putString("direccion", clienteNuevo.getDireccion());
            args.putString("localidad", clienteNuevo.getLocalidad());
            args.putInt("codpos", clienteNuevo.getCodpos());
            //args.putInt("fecnac", clienteNuevo.getFecnac());
        }
        nuevoClienteFragment.setArguments(args);
        return nuevoClienteFragment;
    }

    private void setearDatos(long dni, int categoria, String nombre, String correo, String telefono, String direccion, String localidad, int codpos){
        edtNuevoClienteDni.setText(""+ dni);

        String descripcion = leerCategoria(categoria);
        String elemento = categoria + " - " + descripcion;

        spNuevoClienteCategorias.setSelection(arrayAdapter.getPosition(elemento));
        edtNuevoClienteNombre.setText(nombre);
        edtNuevoClienteCorreo.setText(correo);
        edtNuevoClienteTelefono.setText(telefono);
        edtNuevoClienteDireccion.setText(direccion);
        edtNuevoClienteLocalidad.setText(localidad);
        edtNuevoClienteCodpos.setText("" + codpos);
        /*
        String fecnacS = "" + fecnac;
        String fecnacSS = fecnacS.substring(6, 8) + "/" + fecnacS.substring(4, 6) + "/" + fecnacS.substring(0, 4);
        edtNuevoClienteFechaNacimiento.setText(fecnacSS);
        */
    }

    private void limpiarDatos(){
        editarInfo = false;
        edtNuevoClienteDni.setText("");
        spNuevoClienteCategorias.setSelection(0); //seleccionamos el 1er item del spinner
        edtNuevoClienteNombre.setText("");
        edtNuevoClienteCorreo.setText("");
        edtNuevoClienteTelefono.setText("");
        edtNuevoClienteDireccion.setText("");
        edtNuevoClienteLocalidad.setText("");
        edtNuevoClienteCodpos.setText("");
        //edtNuevoClienteFechaNacimiento.setText(generarFecha(sDay,sMonth,sYear));
        edtNuevoClienteDni.requestFocus();
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

}
