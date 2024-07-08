package kt.distribuidoraBJ.menu_lateral;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import kt.distribuidoraBJ.R;
import kt.distribuidoraBJ.sql.AdminSQLiteOpenHelper;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static kt.distribuidoraBJ.constantes.constantes.*;

@SuppressLint("ValidFragment")
public class ImportarDatosFragment extends Fragment {

    private Button btnImportarClientes;
    private TextView tvPathClientes;
    private Button btnImportarArticulos;
    private TextView tvPathArt;
    private Button btnImportarCategorias;
    private TextView tvPathCategorias;
    private Button btnImportarZonas;
    private TextView tvPathZonas;
    private Button btnImportar;

    private Context contexto;

    private Uri pathArt;
    private Uri pathClientes;
    private Uri pathVendedores;
    private Uri pathCategorias;
    private Uri pathZonas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_importar_datos, container, false);

        contexto = container.getContext();

        btnImportar = (Button) view.findViewById(R.id.btnImportar);
        btnImportarClientes = (Button) view.findViewById(R.id.btnImportarClientes);
        tvPathClientes = (TextView) view.findViewById(R.id.tvPathClientes);
        btnImportarArticulos = (Button) view.findViewById(R.id.btnImportarArticulos);
        tvPathArt = (TextView) view.findViewById(R.id.tvPathArt);
        btnImportarCategorias = (Button) view.findViewById(R.id.btnImportarCategorias);
        tvPathCategorias = (TextView) view.findViewById(R.id.tvPathCategorias);
        btnImportarZonas = (Button) view.findViewById(R.id.btnImportarZonas);
        tvPathZonas = (TextView) view.findViewById(R.id.tvPathZonas);

        FLAG_IMPORTA_DATOS = 0;

        btnImportarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Archivo Clientes"), RESULT_CLIENTES_FILE);
            }
        });
        btnImportarArticulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Archivo Articulos"), RESULT_ARTICULOS_FILE);
            }
        });

        btnImportarCategorias.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                 intent.setType("*/*");
                 startActivityForResult(Intent.createChooser(intent, "Archivo Categorias"), RESULT_CATEGORIAS_FILE);
             }
        });

        btnImportarZonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Archivo Zonas"), RESULT_ZONAS_FILE);
            }
        });

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvPathClientes.setError(null);
                tvPathCategorias.setError(null);
                tvPathArt.setError(null);
                if (!tvPathClientes.getText().toString().isEmpty() || !tvPathArt.getText().toString().isEmpty() || !tvPathCategorias.getText().toString().isEmpty() || !tvPathZonas.getText().toString().isEmpty() ) {
                    Toast.makeText(contexto, "Importando Datos", Toast.LENGTH_SHORT).show();
                    if (importarCSV()) {
                        Toast.makeText(contexto, "Los Datos fueron Importados EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                        FLAG_IMPORTA_DATOS = 1;
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(contexto, "ERROR al importar los Datos. Vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                    }

                    tvPathClientes.setText("");
                    tvPathClientes.setHint(R.string.edtImportarClientesPath);
                    tvPathArt.setText("");
                    tvPathArt.setHint(R.string.edtImportarArticulosPath);
                    tvPathCategorias.setText("");
                    tvPathCategorias.setHint(R.string.edtImportarCategoriasPath);
                    tvPathZonas.setText("");
                    tvPathZonas.setHint(R.string.edtImportarZonasPath);

                } else {
                    Toast.makeText(contexto, "Debe seleccionar al menos un Archivo", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
            if(requestCode == RESULT_CLIENTES_FILE) {
                tvPathClientes.setText("");
                tvPathClientes.setHint(R.string.edtImportarClientesPath);
            }
            if(requestCode == RESULT_ARTICULOS_FILE) {
                tvPathArt.setText("");
                tvPathArt.setHint(R.string.edtImportarArticulosPath);
            }
            if(requestCode == RESULT_CATEGORIAS_FILE) {
                tvPathCategorias.setText("");
                tvPathCategorias.setHint(R.string.edtImportarCategoriasPath);
            }
            if(requestCode == RESULT_ZONAS_FILE) {
                tvPathZonas.setText("");
                tvPathZonas.setHint(R.string.edtImportarZonasPath);
            }
        }


        if (resultCode == RESULT_OK){
            if(requestCode == RESULT_CLIENTES_FILE) {
                //Procesar el resultado
                pathClientes = data.getData(); //obtener el uri content
                tvPathClientes.setText(pathClientes.getPath());
                /*
                String extension = pathClientes.getPath().substring(pathClientes.getPath().lastIndexOf("."));
                if (!extension.toUpperCase().equals(".CSV")) {
                    Toast.makeText(contexto, "Debe elegir un archivo .CSV", Toast.LENGTH_SHORT).show();
                    tvPathClientes.setText("");
                    tvPathClientes.setHint(R.string.edtImportarClientesPath);
                } else {
                    if (!pathClientes.getPath().toUpperCase().contains("CLIENTES")) {
                        Toast.makeText(contexto, "Debe elegir un archivo de CLIENTES", Toast.LENGTH_SHORT).show();
                        tvPathClientes.setText("");
                        tvPathClientes.setHint(R.string.edtImportarClientesPath);

                    } else {
                        tvPathClientes.setText(pathClientes.getPath());
                    }
                }
                */
            }
            if(requestCode == RESULT_ARTICULOS_FILE) {
                //Procesar el resultado
                pathArt = data.getData(); //obtener el uri content
                tvPathArt.setText(pathArt.getPath());
                /*
                String extension = pathArt.getPath().substring(pathArt.getPath().lastIndexOf("."));
                if (!extension.toUpperCase().equals(".CSV")) {
                    Toast.makeText(contexto, "Debe elegir un archivo .CSV", Toast.LENGTH_SHORT).show();
                    tvPathArt.setText("");
                    tvPathArt.setHint(R.string.edtImportarArticulosPath);
                } else {
                    if (!pathArt.getPath().toUpperCase().contains("ARTICULOS")) {
                        Toast.makeText(contexto, "Debe elegir un archivo de ARTICULOS", Toast.LENGTH_SHORT).show();
                        tvPathArt.setText("");
                        tvPathArt.setHint(R.string.edtImportarArticulosPath);

                    } else {
                        tvPathArt.setText(pathArt.getPath());
                    }
                }
                */
            }
            if(requestCode == RESULT_CATEGORIAS_FILE) {
                //Procesar el resultado
                pathCategorias = data.getData(); //obtener el uri content
                tvPathCategorias.setText(pathCategorias.getPath());
                /*
                String extension = pathCategorias.getPath().substring(pathCategorias.getPath().lastIndexOf("."));
                if (!extension.toUpperCase().equals(".CSV")) {
                    Toast.makeText(contexto, "Debe elegir un archivo .CSV", Toast.LENGTH_SHORT).show();
                    tvPathCategorias.setText("");
                    tvPathCategorias.setHint(R.string.edtImportarCategoriasPath);
                } else {
                    if (!pathCategorias.getPath().toUpperCase().contains("CATEGORIAS")) {
                        Toast.makeText(contexto, "Debe elegir un archivo de CATEGORIAS", Toast.LENGTH_SHORT).show();
                        tvPathCategorias.setText("");
                        tvPathCategorias.setHint(R.string.edtImportarCategoriasPath);

                    } else {
                        tvPathCategorias.setText(pathCategorias.getPath());
                    }
                }
                */
            }
            if(requestCode == RESULT_ZONAS_FILE) {
                //Procesar el resultado
                pathZonas = data.getData(); //obtener el uri content
                tvPathZonas.setText(pathZonas.getPath());
                /*
                String extension = pathCategorias.getPath().substring(pathCategorias.getPath().lastIndexOf("."));
                if (!extension.toUpperCase().equals(".CSV")) {
                    Toast.makeText(contexto, "Debe elegir un archivo .CSV", Toast.LENGTH_SHORT).show();
                    tvPathCategorias.setText("");
                    tvPathCategorias.setHint(R.string.edtImportarCategoriasPath);
                } else {
                    if (!pathCategorias.getPath().toUpperCase().contains("CATEGORIAS")) {
                        Toast.makeText(contexto, "Debe elegir un archivo de CATEGORIAS", Toast.LENGTH_SHORT).show();
                        tvPathCategorias.setText("");
                        tvPathCategorias.setHint(R.string.edtImportarCategoriasPath);

                    } else {
                        tvPathCategorias.setText(pathCategorias.getPath());
                    }
                }
                */
            }
        }
    }


    public boolean importarCSV() {
        boolean retorno = false;
        String cadena;
        String[] arreglo;

        if (!tvPathArt.getText().toString().isEmpty()) {
            limpiarTablas("articulos");
            cadena = "";
            arreglo = null;
            if (procesarArchivoArticulos(cadena, arreglo)){
                retorno = true;
            } else {
                retorno = false;
                return retorno;
            }
        }
        if (!tvPathClientes.getText().toString().isEmpty()) {
            limpiarTablas("clientes");
            limpiarTablas("clientesNuevos");
            cadena = "";
            arreglo = null;
            if (procesarArchivoClientes(cadena, arreglo)){
                retorno = true;
            } else {
                retorno = false;
                return retorno;
            }
        }
        if (!tvPathCategorias.getText().toString().isEmpty()) {
            limpiarTablas("categorias");
            cadena = "";
            arreglo = null;
            if (procesarArchivoCategorias(cadena, arreglo)){
              retorno = true;
            } else {
                retorno = false;
                return retorno;
            }
        }
        if (!tvPathZonas.getText().toString().isEmpty()) {
            limpiarTablas("zonas");
            cadena = "";
            arreglo = null;
            if (procesarArchivoZonas(cadena, arreglo)){
                retorno = true;
            } else {
                retorno = false;
                return retorno;
            }
        }
        return retorno;
    }

    private boolean procesarArchivoClientes(String cadena, String[] arreglo){
        boolean retorno = false;
        try {
            InputStream inputStream = contexto.getContentResolver().openInputStream(pathClientes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
            String linea;

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            int numLinea = 0;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                numLinea++;
                if (numLinea <= 2){
                    if (numLinea == 1 && !linea.toUpperCase().contains("CLIENTES")) {
                        return retorno;
                    }
                    continue;
                }
                arreglo = linea.split(",");

                if (arreglo[0].isEmpty()) {
                    continue;
                }

                long codigo = Long.parseLong(arreglo[0]);
                String nombre = arreglo[1];
                int codigoLista = 0;
                if (!arreglo[2].isEmpty()) {
                    codigoLista = Integer.parseInt(arreglo[2]);
                }
                double costo = 0;
                if (!arreglo[3].isEmpty()) {
                    costo = Double.parseDouble(arreglo[3]);
                }
                String fcLista = arreglo[4];
                boolean facturaConLista = false;
                if (fcLista.toUpperCase().equals("S")) {
                    facturaConLista = true;
                }
                int zona = 0;
                if (!arreglo[5].isEmpty()) {
                    zona = Integer.parseInt(arreglo[5]);
                }

                ContentValues registro = new ContentValues();

                registro.put("codigo", codigo);
                registro.put("nombre", nombre);
                registro.put("codigoLista", codigoLista);
                registro.put("costo", costo);
                registro.put("facturaConLista", facturaConLista);
                registro.put("zona", zona);


                //listaClientes.add( new Cliente(codigo, nombre, codigoLista, costo, facturaConLista));

                // los inserto en la base de datos
                db.insert("clientes", null, registro);

            }
            inputStream.close();
            db.close();
            //Toast.makeText(contexto, "Los Datos de los Clientes fueron Importados EXITOSAMENTE", Toast.LENGTH_SHORT).show();
            retorno = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(contexto, "ERROR al importar los Clientes", Toast.LENGTH_SHORT).show();
            tvPathClientes.setError("ERROR al importar los Clientes");
        } finally {
            return retorno;
        }
    }

    private boolean procesarArchivoArticulos(String cadena, String[] arreglo){
        boolean retorno = false;
        try {
            InputStream inputStream = contexto.getContentResolver().openInputStream(pathArt);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
            String linea;

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            int numLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numLinea++;
                if (numLinea <= 2){
                    if (numLinea == 1 && !linea.toUpperCase().contains("ARTICULOS")) {
                        return retorno;
                    }
                    continue;
                }
                arreglo = linea.split(",");

                if (arreglo[0].isEmpty()) {
                    continue;
                }

                int codigo = Integer.parseInt(arreglo[0]);
                String descripcion = arreglo[1];
                double costo = 0;
                if (!arreglo[2].isEmpty()) {
                    costo = Double.parseDouble(arreglo[2]);
                }
                int codigoLista = 0;
                if (!arreglo[3].isEmpty()) {
                    codigoLista = Integer.parseInt(arreglo[3]);
                }
                double precio = 0;
                if (!arreglo[4].isEmpty()) {
                    precio = Double.parseDouble(arreglo[4]);
                }

                ContentValues registro = new ContentValues();

                registro.put("codigo", codigo);
                registro.put("descripcion", descripcion);
                registro.put("costo", costo);
                registro.put("codigoLista", codigoLista);
                registro.put("precio", precio);


                //listaArticulos.add( new Articulo(codigo, descripcion, costo, codigoLista, precio));

                // los inserto en la base de datos
                db.insert("articulos", null, registro);

            }
            inputStream.close();
            db.close();
            //Toast.makeText(contexto, "Los Datos de los Articulos fueron Importados EXITOSAMENTE", Toast.LENGTH_SHORT).show();
            retorno = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(contexto, "ERROR al importar los Articulos", Toast.LENGTH_SHORT).show();
            tvPathArt.setError("ERROR al importar los Articulos");
        } finally {
            return retorno;
        }
    }

    private boolean procesarArchivoCategorias(String cadena, String[] arreglo){
        boolean retorno = false;
        try {
            InputStream inputStream = contexto.getContentResolver().openInputStream(pathCategorias);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
            String linea;

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            int numLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numLinea++;
                if (numLinea <= 2){
                    if (numLinea == 1 && !linea.toUpperCase().contains("CATEGORIAS")) {
                        return retorno;
                    }
                    continue;
                }
                arreglo = linea.split(",");

                if (arreglo[0].isEmpty()) {
                    continue;
                }

                int codigo = Integer.parseInt(arreglo[0]);
                String descripcion = arreglo[1];

                ContentValues registro = new ContentValues();

                registro.put("codigo", codigo);
                registro.put("descripcion", descripcion);

                // los inserto en la base de datos
                db.insert("categorias", null, registro);

            }
            inputStream.close();
            db.close();
            //Toast.makeText(contexto, "Los Datos de las Categorias fueron Importados EXITOSAMENTE", Toast.LENGTH_SHORT).show();
            retorno = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(contexto, "ERROR al importar las Categorias", Toast.LENGTH_SHORT).show();
            tvPathCategorias.setError("ERROR al importar las Categorias");
        } finally {
            return retorno;
        }
    }

    private boolean procesarArchivoZonas(String cadena, String[] arreglo){
        boolean retorno = false;
        try {
            InputStream inputStream = contexto.getContentResolver().openInputStream(pathZonas);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
            String linea;

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            int numLinea = 0;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                numLinea++;
                if (numLinea <= 2){
                    if (numLinea == 1 && !linea.toUpperCase().contains("ZONAS")) {
                        return retorno;
                    }
                    continue;
                }
                arreglo = linea.split(",");

                if (arreglo[0].isEmpty()) {
                    continue;
                }

                int codigo = Integer.parseInt(arreglo[0]);
                String descripcion = arreglo[1];

                ContentValues registro = new ContentValues();

                registro.put("codigo", codigo);
                registro.put("descripcion", descripcion);

                // los inserto en la base de datos
                db.insert("zonas", null, registro);

            }
            inputStream.close();
            db.close();
            //Toast.makeText(contexto, "Los Datos de las Categorias fueron Importados EXITOSAMENTE", Toast.LENGTH_SHORT).show();
            retorno = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(contexto, "ERROR al importar las Categorias", Toast.LENGTH_SHORT).show();
            tvPathCategorias.setError("ERROR al importar las Zonas");
        } finally {
            return retorno;
        }
    }
    public void limpiarTablas(String tabla) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        admin.borrarRegistros(tabla, db);
    }

}
