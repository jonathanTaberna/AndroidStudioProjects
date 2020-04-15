package com.xcheko51x.importar_csv_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvUsuarios;
    Button btnImportar;
    List<Usuario> listaUsuarios = new ArrayList<>();
    AdaptadorUsuarios adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvUsuarios = findViewById(R.id.rvUsuarios);
        btnImportar = findViewById(R.id.btnImportar);
        rvUsuarios.setLayoutManager(new GridLayoutManager(this, 1));

        pedirPermisos();

        btnImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importarCSV();
            }
        });

    }

    public void importarCSV() {
        limpiarTablas("usuarios");

        File carpeta = new File(Environment.getExternalStorageDirectory() + "/ExportarSQLiteCSV");

        String archivoAgenda = carpeta.toString() + "/" + "Usuarios.csv";

        boolean isCreate = false;
        if(!carpeta.exists()) {
            Toast.makeText(this, "NO EXISTE LA CARPETA", Toast.LENGTH_SHORT).show();
        } else {
            String cadena;
            String[] arreglo;

            try {
                FileReader fileReader = new FileReader(archivoAgenda);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while((cadena = bufferedReader.readLine()) != null) {

                    arreglo = cadena.split(",");

                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "dbSistema", null, 1);
                    SQLiteDatabase db = admin.getWritableDatabase();

                    ContentValues registro = new ContentValues();

                    registro.put("nombre", arreglo[1]);
                    registro.put("telefono", arreglo[2]);

                    listaUsuarios.add(
                            new Usuario(
                                    arreglo[1],
                                    arreglo[2]
                            )
                    );

                    // los inserto en la base de datos
                    db.insert("usuarios", null, registro);

                    db.close();

                    Toast.makeText(MainActivity.this, "SE IMPORTO EXITOSAMENTE", Toast.LENGTH_SHORT).show();

                    adaptador = new AdaptadorUsuarios(MainActivity.this, listaUsuarios);
                    rvUsuarios.setAdapter(adaptador);

                }
            } catch(Exception e) { }
        }
    }

    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);

        }
    }

    public void limpiarTablas(String tabla) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "dbSistema", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        admin.borrarRegistros(tabla, db);

        Toast.makeText(MainActivity.this, "Se limpio los registros de la "+tabla, Toast.LENGTH_SHORT).show();
    }
}
