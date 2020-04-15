package kt.distribuidora.menu_lateral;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import kt.distribuidora.MainActivity;
import kt.distribuidora.R;
import kt.distribuidora.constantes.constantes;
import kt.distribuidora.fragments_secundarios.OpcionesExportacionDialogo;
import kt.distribuidora.fragments_secundarios.SeleccionarClienteDialogo;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;
import kt.distribuidora.elementos.Articulo;

@SuppressLint("ValidFragment")
public class MenuFragment extends Fragment { //implements View.OnClickListener {

    //variables main activity

    private Context contexto;
    private Button btnNuevoPedido;
    private Button btnExportarPedidos;

    private SeleccionarClienteDialogo.FinalizoSeleccionarClienteDialogo finalizoSeleccionarClienteDialogo;
    private OpcionesExportacionDialogo.FinalizoOpcionesExportacionDialogo finalizoOpcionesExportacionDialogo;

    NavigationView navigationView;

    //fin variables main activity
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu, container, false);

        contexto = container.getContext();

        btnNuevoPedido = (Button) view.findViewById(R.id.btnNuevoPedido);
        btnExportarPedidos = (Button) view.findViewById(R.id.btnExportarPedidos);

        finalizoSeleccionarClienteDialogo = new SeleccionarClienteDialogo.FinalizoSeleccionarClienteDialogo() {
            @Override
            public void ResultadoSeleccionarClienteDialogo(long codigoCliente, String nombreCliente) {
                //Toast.makeText(contexto,"codigo cliente: " + codigoCliente + " - " + nombreCliente, Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).ResultadoBotoneraMainMenu(constantes.RESULT_NUEVO_PEDIDO, codigoCliente, nombreCliente);

            }
        };
        finalizoOpcionesExportacionDialogo = new OpcionesExportacionDialogo.FinalizoOpcionesExportacionDialogo() {
            @Override
            public void ResultadoOpcionesExportacionDialogo(int resultadoExportacion) {
               generarCSVpedidos(resultadoExportacion);
            }
        };

        btnNuevoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SeleccionarClienteDialogo(contexto, finalizoSeleccionarClienteDialogo, null, null);


                //((MainActivity) getActivity()).ResultadoBotoneraMainMenu(constantes.RESULT_NUEVO_PEDIDO);
                return;
            }
        });
        btnExportarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OpcionesExportacionDialogo(contexto, finalizoOpcionesExportacionDialogo, 0);
                return;
            }
        });

        /*
        btnExportarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                // Set a title for alert dialog
                builder.setTitle("Generar Pedidos.");
                builder.setCancelable(false);

                // Ask the final question
                builder.setMessage("Desea generar un archivo con los pedidos cargados?");

                builder.setNeutralButton("Generar y Borrar Pedidos Cargados", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //generarCSVpedidos();
                    }
                });

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button

                        generarCSVpedidos();
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
        */
        /*
        btnExportarPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto, android.R.style.Theme_DeviceDefault_Dialog); //Theme_DeviceDefault_Light_Dialog

                // String array for alert dialog multi choice items
                String[] opciones = new String[]{
                        "Exportar y Borrar los Pedidos cargados",
                        "Exportar y Guardar los Pedidos cargados a modo de Backup",
                        "Solo Exportar"
                };
                //String[] colors = new String[]{
                //        "Red",
                //        "Green",
                //        "Blue",
                //        "Purple",
                //        "Olive"
                //};

                // Boolean array for initial selected items
                final boolean[] checkedOpciones = new boolean[]{
                        false, // Exportar y Borrar los Pedidos cargados
                        false, // Exportar y Guardar los Pedidos cargados en otra tabla
                        true // Solo Exportar
                };

                // Convert the color array to list
                final List<String> opcionesList = Arrays.asList(opciones);

                // Set multiple choice items for alert dialog
                builder.setMultiChoiceItems(opciones, checkedOpciones, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        // Update the current focused item's checked status
                        checkedOpciones[which] = isChecked;
                        checkedOpciones[0] = false;

                        // Get the current focused item
                        //String currentItem = opcionesList.get(which);

                        // Notify the current action
                        //Toast.makeText(contexto, currentItem + " " + isChecked, Toast.LENGTH_SHORT).show();
                    }
                });

                // Specify the dialog is not cancelable
                builder.setCancelable(false);

                // Set a title for alert dialog
                builder.setTitle("Generar Pedidos");

                // Set the positive/yes button click listener
                builder.setPositiveButton(R.string.btnAceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click positive button
                        //Toast.makeText(contexto, "ok button", Toast.LENGTH_SHORT).show();
                        generarCSVpedidos(checkedOpciones);
                    }
                });

                // Set the negative/no button click listener
                builder.setNegativeButton(R.string.btnCancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click the negative button
                    }
                });

                // Set the neutral/cancel button click listener
                //builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                //    @Override
                //    public void onClick(DialogInterface dialog, int which) {
                //        // Do something when click the neutral button
                //    }
                //});


                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });
        */




        return view;
    }


    //private void generarCSVpedidos(boolean[] checkedOpciones) {
    private void generarCSVpedidos(int resultadoExportacion) {
        try {
            String baseFolder;
// check if external storage is available
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                baseFolder = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            }
// revert to using internal storage (not sure if there's an equivalent to the above)
            else {
                baseFolder = contexto.getFilesDir().getAbsolutePath();
            }

            //formatear numero con ceros significativos
            Formatter mesFormater = new Formatter();
            Formatter diaFormater = new Formatter();
            Formatter horaFormater = new Formatter();
            Formatter minutosFormater = new Formatter();
            Formatter segundosFormater = new Formatter();
            Formatter codigoVendedorArchFormater = new Formatter();

            Calendar c = Calendar.getInstance();
            int sYear = c.get(Calendar.YEAR);
            int sMonth = c.get(Calendar.MONTH);
            sMonth++;
            int sDay = c.get(Calendar.DAY_OF_MONTH);

            mesFormater.format("%02d",sMonth);
            diaFormater.format("%02d",sDay);

            long codigoVendedorArch = 0;
            AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(getContext(), "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

            //hora en formato HHMMSS
            Calendar calendario = Calendar.getInstance();
            int hora, minutos, segundos;
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            segundos = calendario.get(Calendar.SECOND);

            horaFormater.format("%02d",hora);
            minutosFormater.format("%02d",minutos);
            segundosFormater.format("%02d",segundos);


            String query = "SELECT codigo FROM vendedores";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                codigoVendedorArch = cursor.getLong(0);
            } else {
                Toast.makeText(contexto, "Error en la generacion del archivo de Pedidos", Toast.LENGTH_LONG).show();
                return;
            }

            codigoVendedorArchFormater.format("%06d",codigoVendedorArch);


            String nombreArch = "Pedido" + sYear + mesFormater + diaFormater + horaFormater + minutosFormater + segundosFormater + codigoVendedorArchFormater + ".CSV";

            File file = new File(baseFolder + File.separator + nombreArch);
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);


            db = adminSQLiteOpenHelper.getReadableDatabase();

            query = "SELECT idPedido, codigoVendedor, nombreVendedor, codigoCliente, nombreCliente, codigoListaPrecios, fechaPedido, comentariosPedido " +
                    "FROM pedidos";
            cursor = db.rawQuery(query, null);
            int cont = 0;
            int idPedidoAnterior = 0;

            boolean primerInsert = true;
            String linea = "";

            linea = "idPedido, codigoVendedor, nombreVendedor, codigoCliente, nombreCliente, listaPrecios, fechaPedido, comentariosPedido, codigoProducto, nombreProducto, cantidadProducto, precioProducto, cantidadProductoBonif" + "\n";
            fos.write(linea.getBytes());

            while (cursor.moveToNext()) {
                int idPedido = cursor.getInt(0);
                int codigoVendedor = cursor.getInt(1);
                String nombreVendedor = cursor.getString(2);
                int codigoCliente = cursor.getInt(3);
                String nombreCliente = cursor.getString(4);
                int listaPrecios = cursor.getInt(5);
                int fechaPedido = cursor.getInt(6);
                String comentariosPedido = cursor.getString(7);

                /*
                if (idPedidoAnterior != idPedido) {
                    idPedidoAnterior = idPedido;
                    linea = "idPedido, codigoVendedor, nombreVendedor, codigoCliente, nombreCliente, fechaPedido, comentariosPedido" + "\n";
                    fos.write(linea.getBytes());
                    linea = idPedido + "," + codigoVendedor + "," + nombreVendedor + "," + codigoCliente + "," + nombreCliente + "," + fechaPedido + "," + comentariosPedido + "\n";
                    fos.write(linea.getBytes());
                }
                */

                ArrayList<Articulo> listaArticulos = new ArrayList<>();
                SQLiteDatabase dbArt = adminSQLiteOpenHelper.getReadableDatabase();
                String queryArt = "SELECT codigoProducto, nombreProducto, cantidadProducto, precioProducto, cantidadProductoBonif " +
                                    "FROM productosPedidos " +
                                    "WHERE idPedido = " + idPedido ;
                Cursor cursorArt = dbArt.rawQuery(queryArt, null);
                primerInsert = true;
                while (cursorArt.moveToNext()) {
                    /*
                    if (primerInsert) {
                        primerInsert = false;
                        linea = "codigoProducto, nombreProducto, cantidadProducto, precioProducto, cantidadProductoBonif" + "\n";
                        fos.write(linea.getBytes());
                    }
                    */

                    int codigo = cursorArt.getInt(0);
                    String descripcion = cursorArt.getString(1);
                    int cantidad = cursorArt.getInt(2);
                    double precio = cursorArt.getDouble(3);
                    int cantidadBonif = cursorArt.getInt(4);


                    primerInsert = false;
                    /*
                    if (idPedidoAnterior != idPedido) {
                        idPedidoAnterior = idPedido;
                        linea = idPedido + "," + codigoVendedor + "," + nombreVendedor + "," + codigoCliente + "," + nombreCliente + "," + listaPrecios + "," + fechaPedido + "," + comentariosPedido + "," + codigo + "," + descripcion + "," + cantidad + "," + precio + "," + cantidadBonif + "\n";
                    } else {
                        linea = "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + codigo + "," + descripcion + "," + cantidad + "," + precio + "," + cantidadBonif + "\n";
                    }
                    */
                    if (idPedidoAnterior != idPedido) {
                        idPedidoAnterior = idPedido;
                    }
                    linea = idPedido + "," + codigoVendedor + "," + nombreVendedor + "," + codigoCliente + "," + nombreCliente + "," + listaPrecios + "," + fechaPedido + "," + comentariosPedido + "," + codigo + "," + descripcion + "," + cantidad + "," + precio + "," + cantidadBonif + "\n";
                    fos.write(linea.getBytes());
                }
                if (primerInsert) {
                    linea = idPedido + "," + codigoVendedor + "," + nombreVendedor + "," + codigoCliente + "," + nombreCliente + "," + listaPrecios + "," + fechaPedido + "," + comentariosPedido + "\n";
                    fos.write(linea.getBytes());
                }

                linea =  "\n";;
                fos.write(linea.getBytes());
                fos.write(linea.getBytes());
                cursorArt.close();
                cont++;
            }

            cursor.close();


            fos.flush();
            fos.close();

            Toast.makeText(contexto, "Pedido generado en la carpeta de 'Descargas'", Toast.LENGTH_LONG).show();

            /*
            switch (resultadoExportacion){
                case 1:
                    borrarRegistrosPedidos();
                    break;
                case 2:
                    if(!guardarRegistrosPedidos()){
                        Toast.makeText(contexto, "ERROR al hacer Backup de los pedidos exportados", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            */
            switch (resultadoExportacion){
                case 1:
                    if(!guardarRegistrosPedidos()){
                        Toast.makeText(contexto, "ERROR al hacer Backup de los pedidos exportados", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            //if (checkedOpciones[0]) { // Exportar y Borrar los Pedidos cargados
            //}
            // Exportar y Guardar los Pedidos cargados en otra tabla
            // Solo Exportar

        } catch (Exception e) {
            Log.i("FILE-ERROR: ", e.getMessage());
        }
    }

    private void borrarRegistrosPedidos(){

        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        // Set a title for alert dialog
        builder.setTitle("Borra Pedidos.");
        builder.setCancelable(false);

        // Ask the final question
        builder.setMessage("Está seguro de querer BORRAR los pedidos cargados?");

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when user clicked the Yes button
                AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
                SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
                adminSQLiteOpenHelper.borrarRegistros("pedidos", db);
                adminSQLiteOpenHelper.borrarRegistros("productosPedidos", db);
                Toast.makeText(contexto, "Pedidos eliminados correctamente", Toast.LENGTH_LONG).show();
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

    private boolean guardarRegistrosPedidos(){
        boolean retorno = false;
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            String query = "INSERT INTO pedidosGuardados SELECT * FROM pedidos";
            db.execSQL(query);
            //Cursor cursor = db.rawQuery(query, null);
            //cursor.close();

            query = "INSERT INTO productosPedidosGuardados SELECT * FROM productosPedidos";
            db.execSQL(query);

            query = "DELETE FROM pedidos";
            db.execSQL(query);

            query = "DELETE FROM productosPedidos";
            db.execSQL(query);

            //cursor = db.rawQuery(query, null);
            //cursor.close();
            db.close();
            admin.close();
            retorno = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return retorno;
        }
    }



}
