package kt.distribuidora;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kt.distribuidora.constantes.constantes;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;

import static kt.distribuidora.constantes.constantes.RESULT_CERRAR_SESION;
import static kt.distribuidora.constantes.constantes.RESULT_VENDEDORES_FILE;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    //private UserLoginTask mAuthTask = null;
    private int status = 0;

    private EditText mUusarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //private CheckBox cbRecordarUsuario;
    private Button mIngresarButton;
    private Button mImportarInfoButton;
    private ImageView mImageView;
    private AppBarLayout mAppBarLayout;

    //private String estadoArchivo = "";
    private Context contexto;
    private String usuario = "";

    private int ultimoPedidoGuardado;

    //private String usuarioGuardado = "";
    //private String passwordGuardada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contexto = this;

        mAppBarLayout = (AppBarLayout) findViewById(R.id.bar_layout);
        mImageView = (ImageView) findViewById(R.id.toolbar_logo);

        mUusarioView = (EditText) findViewById(R.id.edtUsuario);
        mPasswordView = (EditText) findViewById(R.id.edtPassword);
        mIngresarButton = (Button) findViewById(R.id.btnIngresar);
        mImportarInfoButton = (Button) findViewById(R.id.btnLoginImportarInfo);

        ultimoPedidoGuardado = 0;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
        adminSQLiteOpenHelper.crearTablas(db);
        db.close();
        adminSQLiteOpenHelper.close();

        pedirPermisos();

        //mUusarioView.setEnabled(false);
        mUusarioView.setFocusable(false);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    validarLogin();
                    return true;
                }
                return false;
            }
        });

        mIngresarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (estadoArchivo == constantes.CONFIG_NOT_FOUND) {
                //    generarArchivo(constantes.distribuidoraConfig, mUusarioView.getText().toString(), mPasswordView.getText().toString());
                //}
                //Intent i = new Intent(getBaseContext(), MainActivity.class);
                //startActivityForResult(i,constantes.RESULT_MAIN_ACTIVITY);
                validarLogin();
            }
        });

        mImportarInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getBaseContext(), MainActivity.class);
                //startActivityForResult(i,constantes.RESULT_MAIN_ACTIVITY);
                importarInfoVendedor();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        //cbRecordarUsuario = findViewById(R.id.cbRecordarUsuario);
        mProgressView = findViewById(R.id.login_progress);
        //cbRecordarUsuario.setChecked(true);

        showProgress(false);

        leerDatosVendedor();

        if (usuario.isEmpty()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mImportarInfoButton.setVisibility(View.VISIBLE);
            mPasswordView.setEnabled(false);
            mIngresarButton.setEnabled(false);
        } else {
            mImportarInfoButton.setVisibility(View.INVISIBLE);
            mPasswordView.requestFocus();
            mPasswordView.setEnabled(true);
            mIngresarButton.setEnabled(true);
            mUusarioView.setText(usuario);
        }

        /*
        File archivo = new File(getBaseContext().getFilesDir() + "/" + constantes.distribuidoraConfig);
        if (archivo.exists()) {
            estadoArchivo = constantes.CONFIG_FOUND;

            mImportarInfoButton.setVisibility(View.INVISIBLE);
            //mUusarioView.setEnabled(true);
            mPasswordView.requestFocus();
            mPasswordView.setEnabled(true);
            mIngresarButton.setEnabled(true);

            //cbRecordarUsuario.setChecked(true);
            //cbRecordarUsuario.setEnabled(false);
            String datosCargados = leerDatosCargados();
            String[] partes = null;
            partes = datosCargados.split("//");
            usuarioGuardado = partes[0];
            //passwordGuardada = partes[1];
            if (!usuarioGuardado.isEmpty()) {
                mUusarioView.setText(usuarioGuardado);
                //mPasswordView.setText(passwordGuardada);
                //mUusarioView.setEnabled(false);
                //mPasswordView.setEnabled(false);
            }
        } else {
            estadoArchivo = constantes.CONFIG_NOT_FOUND;

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mImportarInfoButton.setVisibility(View.VISIBLE);
            mPasswordView.setEnabled(false);
            mIngresarButton.setEnabled(false);

            //cbRecordarUsuario.setEnabled(true);
            //mUusarioView.setEnabled(true);
            //mPasswordView.setEnabled(true);
        }
        */

    }

    private void importarInfoVendedor() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Archivo Vendedores"), RESULT_VENDEDORES_FILE);


    }

    /*
    private void guardarDatos() {

        try {
            FileInputStream fis = getBaseContext().openFileInput(constantes.distribuidoraConfig);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("usuario_utilizado")) {
                    line = "usuario_utilizado:" + mUusarioView.getText().toString();
                    //if (cbRecordarUsuario.isChecked()) {
                    //    line = "usuario_utilizado:" + mUusarioView.getText().toString();
                    //} else {
                    //    line = "usuario_utilizado:";
                    //}
                }
                if (line.contains("password_utilizada")) {
                    line = "password_utilizada:" + mPasswordView.getText().toString();
                    //if (cbRecordarUsuario.isChecked()) {
                    //    line = "password_utilizada:" + mPasswordView.getText().toString();
                    //} else {
                    //    line = "password_utilizada:";
                    //}
                }
                sb.append(line + "\n");
            }
            fis.close();
            isr.close();
            bufferedReader.close();

            FileOutputStream mOutput = openFileOutput(constantes.distribuidoraConfig, Context.MODE_PRIVATE);
            mOutput.write(sb.toString().getBytes());
            mOutput.flush();
            mOutput.close();


        } catch (Exception e) {
            Log.i("FILE-ERROR: ", e.getMessage());
        }
    }

    private String leerDatosCargados() {
        String retorno = "";
        String usuario = "";
        String password = "";
        try {
            FileInputStream mInput = openFileInput(constantes.distribuidoraConfig);
            String[] partes = null;

            InputStreamReader isr = new InputStreamReader(mInput);
            BufferedReader buffreader = new BufferedReader(isr);
            String linea = buffreader.readLine();
            while (linea != null) {
                if (linea.contains("usuario_utilizado:")) {
                    partes = linea.split("usuario_utilizado:");
                    if (partes.length >= 2) {
                        String parte1 = partes[0];
                        String parte2 = partes[1];
                        usuario = partes[1];
                    }
                }
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
        } catch (Exception e) {
            Log.i("CONFIG-NOT-FOUND", e.getMessage());
        }
        retorno = usuario + "//" + password;
        return retorno;
    }

    private boolean generarArchivo(String archivo, String usuario, String password) {
        boolean retorno = false;
        try {
            FileOutputStream mOutput = openFileOutput(archivo, Context.MODE_PRIVATE);
            mOutput.write(("usuario_utilizado:" + usuario).getBytes());
            mOutput.write("\n".getBytes());
            mOutput.write(("password_utilizada:" + password).getBytes());
            mOutput.flush();
            mOutput.close();
            retorno = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            retorno = false;
        } catch (IOException e) {
            e.printStackTrace();
            retorno = false;
        }
        return retorno;
    }

    */


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /*
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        */
    }

    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
       //if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
       //     ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
       // }


        final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mUusarioView, R.string.permission_explain, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS, 10);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        }


    }

    private void leerDatosVendedor() {
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();
        db = adminSQLiteOpenHelper.getReadableDatabase();
        String query = "SELECT nombre " +
                "FROM vendedores ";
        Cursor cursor = db.rawQuery(query, null);
        usuario = "";
        if (cursor.moveToFirst()) {
            usuario = cursor.getString(0);
        }
        cursor.close();
    }
    private void validarLogin(){
        // Reset errors.
        mUusarioView.setError(null);
        mPasswordView.setError(null);

        //String usuario = mUusarioView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (password.equals(constantes.passwordKt)) {
            AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();
            adminSQLiteOpenHelper.borrarTablas(db);

            mUusarioView.setText("");
            mPasswordView.setText("");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mImportarInfoButton.setVisibility(View.VISIBLE);
            mPasswordView.setEnabled(false);
            mIngresarButton.setEnabled(false);

            return;
        }

        if (password.toUpperCase().equals(constantes.passwordPm.toUpperCase())) {
            AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
            SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

            String query = "SELECT ultimoPedidoGuardado FROM vendedores";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                ultimoPedidoGuardado = cursor.getInt(0);
            }

            ContentValues registro = new ContentValues();

            registro.put("codigo", 0);
            registro.put("nombre", "");
            registro.put("password", "");
            registro.put("ultimoPedidoGuardado", ultimoPedidoGuardado);
            long result = db.update("vendedores", registro, null, null);

            if (result < 0) { //error al guardar el ultimo pedido
                mPasswordView.setError("Error");
                return;
            }

            //adminSQLiteOpenHelper.borrarRegistros("vendedores", db);
            adminSQLiteOpenHelper.borrarRegistros("pedidosGuardados", db);
            adminSQLiteOpenHelper.borrarRegistros("productosPedidosGuardados", db);

            mUusarioView.setText("");
            mPasswordView.setText("");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mImportarInfoButton.setVisibility(View.VISIBLE);
            mPasswordView.setEnabled(false);
            mIngresarButton.setEnabled(false);

            return;
        }


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user.
        if (TextUtils.isEmpty(usuario)) {
            mUusarioView.setError(getString(R.string.error_field_required));
            focusView = mUusarioView;
            cancel = true;
        }

        /*
        if (usuario.contains(" ")) {
            mUusarioView.setError(getString(R.string.usuario_invalido));
            focusView = mUusarioView;
            cancel = true;
        }
        */

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String query = "SELECT password " +
                        "FROM vendedores " +
                        "WHERE nombre = '" + usuario + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String pass = cursor.getString(0);
            if (!pass.equals(password)) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                focusView = mPasswordView;
                cancel = true;
            }
        }
        cursor.close();


        if (cancel) {
            focusView.requestFocus();
        } else {
            //Toast.makeText(contexto, "login exitoso", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivityForResult(i,constantes.RESULT_MAIN_ACTIVITY);
        }
    }
    private void attemptLogin() {
        /*
        // Reset errors.
        mUusarioView.setError(null);
        mPasswordView.setError(null);

        String usuario = mUusarioView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user.
        if (TextUtils.isEmpty(usuario)) {
            mUusarioView.setError(getString(R.string.error_field_required));
            focusView = mUusarioView;
            cancel = true;
        }

        if (usuario.contains(" ")) {
            mUusarioView.setError(getString(R.string.usuario_invalido));
            focusView = mUusarioView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {

            //showProgress(true);

            Toast.makeText(contexto, "login exitoso", Toast.LENGTH_SHORT).show();
            //showProgress(false);


            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);


            //mAuthTask = new UserLoginTask(usuario, password);
            //mAuthTask.execute((Void) null);
        }

         */
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 0 && password.length() < 15;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == constantes.RESULT_MAIN_ACTIVITY ) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
            /*
            if (resultCode == RESULT_CERRAR_SESION) {
                mPasswordView.setText("");
                mPasswordView.requestFocus();

            }
            */
        }
        if(requestCode == RESULT_VENDEDORES_FILE) {
            //Procesar el resultado
            mPasswordView.setError(null);
            try {
                Uri pathVendedor = data.getData(); //obtener el uri content

                String pathVendedorS = pathVendedor.getPath().toString();
                //Toast.makeText(this, pathVendedorS, Toast.LENGTH_LONG).show();

                String[] arreglo = null;
                try {
                    InputStream inputStream = contexto.getContentResolver().openInputStream(pathVendedor);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
                    String linea;

                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(contexto, "dbSistema", null, 1);
                    SQLiteDatabase db = admin.getWritableDatabase();

                    int numLinea = 0;
                    long codigo = 0;
                    String nombre = "";
                    String password = "";

                    while ((linea = reader.readLine()) != null) {
                        numLinea++;
                        if (numLinea <= 2){
                            if (numLinea == 1 && !linea.toUpperCase().contains("VENDEDOR")) {
                                mPasswordView.setError("Debe elegir un archivo de VENDEDORES");
                                return;
                            }
                            continue;
                        }
                        arreglo = linea.split(",");

                        if (arreglo[0].isEmpty()) {
                            continue;
                        }

                        codigo = Long.parseLong(arreglo[0]);
                        nombre = arreglo[1];
                        password = arreglo[2].trim();

                        ContentValues registro = new ContentValues();

                        registro.put("codigo", codigo);
                        registro.put("nombre", nombre);
                        registro.put("password", password);
                        registro.put("ultimoPedidoGuardado", ultimoPedidoGuardado);

                        String query = "SELECT ultimoPedidoGuardado FROM vendedores";
                        Cursor cursor = db.rawQuery(query, null);

                        long result = 0;
                        if (cursor.moveToFirst()){
                            result = db.update("vendedores", registro, null, null);
                        } else {
                            // los inserto en la base de datos
                            result = db.insert("vendedores", null, registro);
                        }
                        if (result < 0) { //dio error el guardado
                            Toast.makeText(contexto, "ERROR al importar el Vendedor", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                    inputStream.close();
                    db.close();

                    mUusarioView.setText(nombre);
                    mImportarInfoButton.setVisibility(View.INVISIBLE);
                    mPasswordView.setEnabled(true);
                    mPasswordView.requestFocus();
                    mIngresarButton.setEnabled(true);
                    leerDatosVendedor();


                    Toast.makeText(contexto, "Los Datos del Vendedor fueron Importados EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(contexto, "ERROR al importar el Vendedor", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(contexto, "ERROR con el Archivo Seleccionado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

