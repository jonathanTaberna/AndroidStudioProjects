package kt.distribuidora.menu_lateral;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import kt.distribuidora.R;
import kt.distribuidora.constantes.constantes;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;

public class CambiarPassFragment extends Fragment {

    private Context contexto;

    private Button btnCambiarPassAceptar;
    private Button btnCambiarPassCancelar;
    private EditText edtPasswordVieja;
    private EditText edtPasswordNueva;
    private EditText edtPasswordConfirmar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contexto = container.getContext();
        return inflater.inflate(R.layout.activity_cambiar_pass, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCambiarPassAceptar = (Button) view.findViewById(R.id.btnCambiarPassAceptar);
        btnCambiarPassCancelar = (Button) view.findViewById(R.id.btnCambiarPassCancelar);

        edtPasswordVieja = (EditText) view.findViewById(R.id.edtPasswordVieja);
        edtPasswordNueva = (EditText) view.findViewById(R.id.edtPasswordNueva);
        edtPasswordConfirmar = (EditText) view.findViewById(R.id.edtPasswordConfirmar);

        btnCambiarPassAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passVieja = edtPasswordVieja.getText().toString();
                String passNueva = edtPasswordNueva.getText().toString();
                String passConfirma = edtPasswordConfirmar.getText().toString();

                if (!passVieja.equals(leerPassCargada())){ //si la pass vieja ingresada no es la cargada
                    edtPasswordVieja.setError(getString(R.string.error_pass_vieja_incorrecta));
                    edtPasswordVieja.requestFocus();
                    return;
                }
                if(passNueva.equals(passVieja)) {
                    edtPasswordNueva.setError(getString(R.string.error_pass_vieja_nueva));
                    edtPasswordNueva.requestFocus();
                    return;
                }
                if(!passNueva.equals(passConfirma)) {
                    edtPasswordConfirmar.setError(getString(R.string.error_pass_nueva_confira));
                    edtPasswordConfirmar.requestFocus();
                    return;
                }

                guardarContrasenia(passNueva);
            }
        });

        btnCambiarPassCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtPasswordVieja.setText("");
                edtPasswordNueva.setText("");
                edtPasswordConfirmar.setText("");
            }
        });

    }

    private String leerPassCargada(){
        String password = "";
        try {
            /*
            FileInputStream mInput = contexto.openFileInput(constantes.distribuidoraConfig);
            String[] partes = null;

            InputStreamReader isr = new InputStreamReader (mInput) ;
            BufferedReader buffreader = new BufferedReader (isr) ;
            String linea = buffreader.readLine();
            while ( linea != null ) {
                if (linea.contains("password_utilizada:")) {
                    partes = linea.split("password_utilizada:");
                    if (partes.length >= 2) {
                        String parte1 = partes[0];
                        String parte2 = partes[1];
                        password = partes[1];
                    }
                }
                linea = buffreader.readLine(); //lee proxima fila
            }
            mInput.close();
            */





            AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

            String query = "SELECT password " +
                    "FROM vendedores ";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                password = cursor.getString(0);
            }
            cursor.close();









        }
        catch (Exception e) {
            Log.i("CONFIG-NOT-FOUND", e.getMessage());
        }
        return  password;
    }


    private void guardarContrasenia(String pass) {
        try {
            /*
            FileInputStream fis = contexto.openFileInput(constantes.distribuidoraConfig);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("password_utilizada")) {
                    line = "password_utilizada:" + pass;
                }
                sb.append(line + "\n");
            }
            fis.close();
            isr.close();
            bufferedReader.close();

            FileOutputStream mOutput =contexto.openFileOutput(constantes.distribuidoraConfig, Context.MODE_PRIVATE);
            mOutput.write(sb.toString().getBytes());
            mOutput.flush();
            mOutput.close();

            */















            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();

            registro.put("password", pass);

            // los inserto en la base de datos
            db.update("vendedores", registro, null, null);

            db.close();
















            Toast.makeText(contexto, "La Contraseña fue modificada con Éxito", Toast.LENGTH_LONG).show();

            edtPasswordVieja.setText("");
            edtPasswordNueva.setText("");
            edtPasswordConfirmar.setText("");

        } catch (Exception e) {
            Log.i("FILE-ERROR: ", e.getMessage());
        }
    }

}
