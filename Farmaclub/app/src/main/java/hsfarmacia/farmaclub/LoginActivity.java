package hsfarmacia.farmaclub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private int status = 0;
    private JSONObject jsonResp = null;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUusarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox cbRecordarUsuario;

    //preferencias
    private String nroTarjeta = "";
    private String usuarioUtilizado = "";
    private String farmaclubConfig = "farmaclub.config";
    private String estadoArchivo = "";

    //constantes
    private String CONFIG_NOT_FOUND = "CONFIG_NOT_FOUND";
    private String CONFIG_FOUND = "CONFIG_FOUND";
    private int RESULT_NUEVO_USUARIO = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUusarioView = (AutoCompleteTextView) findViewById(R.id.edtUsuario);

        mPasswordView = (EditText) findViewById(R.id.edtPassword);
        /*
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        */

        Button mIngresarButton = (Button) findViewById(R.id.btnIngresar);
        mIngresarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbRecordarUsuario.isChecked()) {
                    if (guardarUsuario()) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(mUusarioView.getWindowToken(), 0);
                        attemptLogin();
                    }
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(mUusarioView.getWindowToken(), 0);
                    attemptLogin();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        cbRecordarUsuario = findViewById(R.id.cbRecordarUsuario);

        File archivo = new File(farmaclubConfig);
        if (archivo.exists()) {
            estadoArchivo = CONFIG_FOUND;
            cbRecordarUsuario.setVisibility(View.VISIBLE);
        } else  {
            cbRecordarUsuario.setVisibility(View.INVISIBLE);
            estadoArchivo = CONFIG_NOT_FOUND;
            mUusarioView.setHint(R.string.edtUsuarioT);
            mUusarioView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        /*
        leerPreferencias();
        if (estadoArchivo == CONFIG_FOUND){
            mUusarioView.setHint(R.string.edtUsuarioU);
            mUusarioView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            mUusarioView.setHint(R.string.edtUsuarioT);
            mUusarioView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        */
    }

    private boolean guardarUsuario() {
        boolean retorno = false;
        List<String> lines = new ArrayList<String>();
        String line = null;

            try {
                File f1 = new File(farmaclubConfig);
                FileReader fr = new FileReader(f1);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {
                    if (line.contains("usuario_utilizado")) {
                        line = "usuario_utilizado: " + mUusarioView.getText().toString();
                    }
                    lines.add(line);
                }
                fr.close();
                br.close();

                FileWriter fw = new FileWriter(f1);
                BufferedWriter out = new BufferedWriter(fw);
                for(String s : lines)
                    out.write(s);
                out.flush();
                out.close();

                retorno = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                retorno = false;
            } catch (IOException e) {
                e.printStackTrace();
                retorno = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                retorno = false;
            }

        return retorno;
    }

    /*
    private void leerPreferencias(String accion){
        try {
            FileInputStream mInput = openFileInput(farmaclubConfig);
            estadoArchivo = CONFIG_FOUND;
            String[] partes = null;

            InputStreamReader isr = new InputStreamReader (mInput) ;
            BufferedReader buffreader = new BufferedReader (isr) ;
            String linea = buffreader.readLine();
            while ( linea != null ) {
                if (linea.contains("nro_tarjeta:")) {
                    partes = linea.split("nro_tarjeta:");
                    String parte1 = partes[0];
                    String parte2 = partes[1];
                }
                if (linea.contains("usuario_utilizado:")) {
                    partes = linea.split("usuario_utilizado:");
                    String parte1 = partes[0];
                    String parte2 = partes[1];
                    mUusarioView.setText(parte2);
                }
                linea = buffreader.readLine(); //lee proxima fila
            }
            mInput.close();
        }
        catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.i("CONFIG-NOT-FOUND", "Archivo no encontrado");
            estadoArchivo = CONFIG_NOT_FOUND;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    private boolean generarArchivo(String archivo) {
        try {
            FileOutputStream mOutput = openFileOutput(archivo, Activity.MODE_PRIVATE);
            mOutput.write(("nro_tarjeta:" + mUusarioView.getText().toString()).getBytes());
            mOutput.flush();
            mOutput.close();
            //mUusarioView.setText("");
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUusarioView.setError(null);
        mPasswordView.setError(null);

        String usuario = mUusarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user.
        if (TextUtils.isEmpty(usuario)) {
            mUusarioView.setError(getString(R.string.error_field_required));
            focusView = mUusarioView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuthTask = new UserLoginTask(usuario, password);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 4 && password.length() < 15;
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
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Nickname
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Nickname.NAME,
                ContactsContract.CommonDataKinds.Nickname.IS_PRIMARY,
        };

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsuario;
        private final String mPassword;

        UserLoginTask(String usuario, String password) {
            mUsuario = usuario;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                //URL url = new URL("http://192.168.2.209:8080/restfull-web-services-app-master/rest/user/u"); //in the real code, there is an ip and a port
                //conn.setRequestMethod("GET");
                //conn.setDoOutput(false);
                URL url;
                if (estadoArchivo == CONFIG_NOT_FOUND){
                    url = new URL("http://192.168.2.50:8080/farmaclubserver/rest/user/validaIni"); //in the real code, there is an ip and a port
                } else {
                    url = new URL("http://192.168.2.50:8080/farmaclubserver/rest/user/valida"); //in the real code, there is an ip and a port
                }

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                if (estadoArchivo == CONFIG_NOT_FOUND){
                    obj.put("tarjeta", mUsuario);
                } else {
                    obj.put("usuario", mUsuario);
                }
                obj.put("pass", mPassword);

                Log.i("JSON", obj.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(obj.toString());

                os.flush();
                os.close();

                status = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                if (status == 200){ //respuesta OK
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");
                        jsonResp = new JSONObject(inputLine);
                    }
                    JsonResponse = buffer.toString();
                    Log.i("RESPONSE",JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    status = 99;
                }
            }catch (SocketTimeoutException e) {
                status = 99;
            } catch (Exception e){
                e.printStackTrace();
            }  finally {
                conn.disconnect();
            }

            if (status == 200) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            int salida = 9;
            String tarjeta = "";
            int puntos = 0;
            String nombre = "";

            try {
                salida = jsonResp.getInt("salida");
                tarjeta = jsonResp.getString("tarjeta");
                puntos = jsonResp.getInt("puntos");
                nombre = jsonResp.getString("nombre");
            } catch (Exception e){
                salida = 9;
                mPasswordView.setError(getString(R.string.error_json));
                mPasswordView.requestFocus();
            }

            switch (salida) {
                case 1:
                    if (success) {
                        if (estadoArchivo == CONFIG_NOT_FOUND) {
                            Intent nuevoUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                            nuevoUsuario.putExtra("tarjeta", tarjeta);
                            nuevoUsuario.putExtra("nombre",nombre);
                            startActivityForResult(nuevoUsuario, RESULT_NUEVO_USUARIO);
                            /*
                            if (generarArchivo(farmaclubConfig)) {
                                Log.i("JONATT", "GENERO BIEN LA CONFIG");
                            } else {
                                Log.i("JONATT", "NO SE GENERO LA CONFIG");
                                break;
                            }
                            */
                        } else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("tarjeta", tarjeta);
                            i.putExtra("puntos", puntos);
                            i.putExtra("nombre", nombre);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                    break;
                case 9:
                    if (status == 99) {
                        mPasswordView.setError(getString(R.string.servidor_timeout));
                        mPasswordView.requestFocus();
                    } else {
                        mPasswordView.setError(getString(R.string.usuario_deconocido));
                        mPasswordView.requestFocus();
                    }
                    break;
                default:
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_NUEVO_USUARIO ) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("RESULT", "RESULT_NUEVO_USUARIO OK");
                mUusarioView.setText(data.getStringExtra("usuario"));
                mPasswordView.setText("");
                if (generarArchivo(farmaclubConfig)) {
                    Log.i("JONATT", "GENERO BIEN LA CONFIG");
                    estadoArchivo = CONFIG_FOUND;
                    cbRecordarUsuario.setVisibility(View.VISIBLE);
                } else {
                    Log.i("JONATT", "NO SE GENERO LA CONFIG");
                }
            } else {
                Log.i("RESULT", "RESULT_NUEVO_USUARIO FAIL");
                mUusarioView.setText("pepito fuma marihuana");
            }
        }
    }

}

