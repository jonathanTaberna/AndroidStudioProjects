package hsneoclinica.neoclinica;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import hsneoclinica.neoclinica.constantes.constantes;
import hsneoclinica.neoclinica.provisorios.Turno;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private int status = 0;
    private UserLoginTask userLoginTaskTask = null;
    private ValidaIniTask validaIniTask = null;
    private String cookie = "";

    // UI references.
    private AutoCompleteTextView mMatriculaView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox cbRecordarMatricula;
    private Button mIngresarButton;
    private TextInputLayout tilPassword;


    private String estadoArchivo = "";
    private String matricula = "";
    private String profesional = "";

    //private ArrayList<Turno> elementos = new ArrayList<Turno>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //createShortCut();

        // Set up the login form.
        mMatriculaView = (AutoCompleteTextView) findViewById(R.id.edtMatricula);
        mPasswordView = (EditText) findViewById(R.id.edtPassword);

        mIngresarButton = (Button) findViewById(R.id.btnIngresar);
        mIngresarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbRecordarMatricula.isChecked()) {
                    guardarMatricula();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mMatriculaView.getWindowToken(), 0);
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tilPassword = findViewById(R.id.tilPassword);
        cbRecordarMatricula = findViewById(R.id.cbRecordarMatricula);

        showProgress(false);

        mMatriculaView.setInputType(InputType.TYPE_CLASS_NUMBER);
        cbRecordarMatricula.setChecked(true);

        File archivo = new File(getBaseContext().getFilesDir()+ "/" + constantes.neoclinicaConfig);
        if (archivo.exists()) {
            estadoArchivo = constantes.CONFIG_FOUND;
            //cbRecordarMatricula.setVisibility(View.VISIBLE);
            //mMatriculaView.setInputType(InputType.TYPE_CLASS_TEXT);
            String matriculaCargada = leerMatriculaCargada();
            if (!matriculaCargada.isEmpty()) {
                mMatriculaView.setText(matriculaCargada);
                mPasswordView.requestFocus();
            }
        } else  {
            estadoArchivo = constantes.CONFIG_NOT_FOUND;
            //cbRecordarMatricula.setVisibility(View.INVISIBLE);
            //mMatriculaView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }

    private void guardarMatricula() {

        try {
            FileInputStream fis = getBaseContext().openFileInput(constantes.neoclinicaConfig);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("nro_profesional")) {
                    if (cbRecordarMatricula.isChecked()) {
                        line = "nro_profesional:" + profesional;
                    } else {
                        line = "nro_profesional:";
                    }
                }
                if (line.contains("matricula_utilizada")) {
                    if (cbRecordarMatricula.isChecked()) {
                        line = "matricula_utilizada:" + mMatriculaView.getText().toString();
                    } else {
                        line = "matricula_utilizada:";
                    }
                }
                sb.append(line + "\n");
            }
            fis.close();
            isr.close();
            bufferedReader.close();

            FileOutputStream mOutput = openFileOutput(constantes.neoclinicaConfig, Context.MODE_PRIVATE);
            mOutput.write(sb.toString().getBytes());
            mOutput.flush();
            mOutput.close();


        } catch (Exception e) {
            Log.i("FILE-ERROR: ", e.getMessage());
        }

    }

    private String leerMatriculaCargada(){
        String matricula = "";
        try {
            FileInputStream mInput = openFileInput(constantes.neoclinicaConfig);
            String[] partes = null;

            InputStreamReader isr = new InputStreamReader (mInput) ;
            BufferedReader buffreader = new BufferedReader (isr) ;
            String linea = buffreader.readLine();
            while ( linea != null ) {
                if (linea.contains("nro_profesional:")) {
                    partes = linea.split("nro_profesional:");
                    if (partes.length >= 2) {
                        String parte1 = partes[0];
                        String parte2 = partes[1];
                        profesional = partes[1];
                    }
                    //mMatriculaView.setText(parte2);
                }
                if (linea.contains("matricula_utilizada:")) {
                    partes = linea.split("matricula_utilizada:");
                    if (partes.length >= 2) {
                        String parte1 = partes[0];
                        String parte2 = partes[1];
                        matricula = partes[1];
                    }
                    //mMatriculaView.setText(parte2);
                }
                linea = buffreader.readLine(); //lee proxima fila
            }
            mInput.close();
        }
        catch (Exception e) {
            Log.i("CONFIG-NOT-FOUND", e.getMessage());
        }
        return  matricula;
    }

    private boolean generarArchivo(String archivo, String matricula, String profesional) {
        boolean retorno = false;
        try {
            FileOutputStream mOutput = openFileOutput(archivo, Context.MODE_PRIVATE);
            mOutput.write(("nro_matricula:" + matricula + "\n").getBytes());
            mOutput.write(("nro_profesional:" + profesional + "\n").getBytes());
            mOutput.write(("matricula_utilizada:").getBytes());
            mOutput.flush();
            mOutput.close();
            retorno = true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            retorno = false;
        }
        catch (IOException e) {
            e.printStackTrace();
            retorno = false;
        }
        return retorno;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (validaIniTask != null) {
            return;
        }

        // Reset errors.
        mMatriculaView.setError(null);
        mPasswordView.setError(null);

        String matricula = mMatriculaView.getText().toString().trim();
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
        if (TextUtils.isEmpty(matricula)) {
            mMatriculaView.setError(getString(R.string.error_field_required));
            focusView = mMatriculaView;
            cancel = true;
        }

        if (matricula.contains(" ")) {
            mMatriculaView.setError(getString(R.string.usuario_invalido));
            focusView = mMatriculaView;
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

            this.matricula = matricula;

            validaIniTask = new ValidaIniTask(matricula, password);
            validaIniTask.execute((Void) null);
            //mAuthTask = new UserLoginTask(matricula, password);
            //mAuthTask.execute((Void) null);
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() >= 1 && password.length() < 15;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == constantes.RESULT_NUEVO_USUARIO ) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("RESULT", "RESULT_NUEVO_USUARIO OK");
                mMatriculaView.setText(data.getStringExtra("usuario"));
                mMatriculaView.setInputType(InputType.TYPE_CLASS_TEXT);
                mPasswordView.setText("");
                if (generarArchivo(constantes.neoclinicaConfig, matricula, profesional)){
                    Log.i("JONATT", "GENERO BIEN LA CONFIG");
                    estadoArchivo = constantes.CONFIG_FOUND;
                    cbRecordarMatricula.setVisibility(View.VISIBLE);
                    cbRecordarMatricula.setChecked(true);
                    mPasswordView.requestFocus();
                } else {
                    Log.i("JONATT", "NO SE GENERO LA CONFIG");
                }
            }
        }

        if (requestCode == constantes.RESULT_MAIN_ACTIVITY ) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
            if (resultCode == Activity.RESULT_FIRST_USER) {
                mPasswordView.setText("");
                mPasswordView.requestFocus();
            }
        }

    }

    public class ValidaIniTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject respuestaValidaIni = new JSONObject();
        private int respuestaStatus = 0;

        private final String mMatricula;
        private final String mPassword;

        ValidaIniTask(String matricula, String password) {
            mMatricula = matricula;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + constantes.metodoHsAgenda);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                Log.i("JSON", obj.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(obj.toString());

                os.flush();
                os.close();

                respuestaStatus = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                if (respuestaStatus == 200){ //respuesta OK
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    cookie = conn.getHeaderField("set-cookie");
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");
                        respuestaValidaIni = new JSONObject(inputLine);
                    }
                    JsonResponse = buffer.toString();
                    Log.i("RESPONSE",JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    respuestaStatus = 99;
                } else {
                    respuestaStatus = 98;
                }
            }catch (SocketTimeoutException e) {
                respuestaStatus = 99;
            } catch (Exception e){
                e.printStackTrace();
            }  finally {
                conn.disconnect();
            }

            if (respuestaStatus == 200) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            validaIniTask = null;
            showProgress(false);

            //variables de respuesta
            String status = "9";
            String msj = "";
            JSONObject jsonObject = null;

            try {
                jsonObject = respuestaValidaIni.getJSONObject("_comm_buffer");

                status = jsonObject.getString("_status");
                msj = jsonObject.getString("_message");
            } catch (Exception e){
                status = "8";
                return;
            }

            switch (status) {
                case "OK":
                    if (success) {

                        userLoginTaskTask = new UserLoginTask(mMatricula, mPassword);
                        userLoginTaskTask.execute((Void) null);

                        /*
                        if (generarArchivo(constantes.neoclinicaConfig, matricula)) {
                            Log.i("JONATT", "GENERO BIEN LA CONFIG");
                            estadoArchivo = constantes.CONFIG_FOUND;
                            guardarMatricula();
                        } else {
                            Log.i("JONATT", "NO SE GENERO LA CONFIG");
                        }

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("matricula", mMatricula);
                        i.putExtra("password", mPassword);
                        i.putExtra("cookie", cookie);
                        startActivityForResult(i,constantes.RESULT_MAIN_ACTIVITY);
                        */

                    } else {
                        mMatriculaView.setError(getString(R.string.servidor_timeout));
                        mMatriculaView.requestFocus();
                    }
                    break;
                default:
                    mMatriculaView.setError(getString(R.string.servidor_timeout));
                    mMatriculaView.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            validaIniTask = null;
            showProgress(false);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mMatricula;
        private final String mPassword;
        private JSONObject jsonResp = null;

        UserLoginTask(String matricula, String password) {
            mMatricula = matricula;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + constantes.metodoValidaProfeIni);// + "get_matricula="+ mMatricula+"&get_pass=" + mPassword);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Cookie",cookie);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("get_matricula", mMatricula);
                obj.put("get_pass", mPassword);

                Log.i("JSON", obj.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(obj.toString());

                os.flush();
                os.close();

                status = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage().toString());

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
                } else {
                    status = 98;
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
            userLoginTaskTask = null;
            showProgress(false);

            JSONObject jsonObject = null;

            int salida = 0;
            int flag = 0;
            String nombre = "";
            String fecha = "";

            try {
                jsonObject = jsonResp.getJSONObject("Response");

                flag = jsonObject.getInt("flag");
                nombre = jsonObject.getString("nombre");
                profesional = jsonObject.getString("profesional");

            } catch (Exception e){
                salida = 8;
            }

            switch (salida) {
                case 0:
                case 8:
                    if (salida == 8) {
                        Log.i("Login", "error en casteo JSON");
                        if (status == 98) {
                            mPasswordView.setError(getString(R.string.servidor_error));
                            mPasswordView.requestFocus();
                            break;
                        }
                    }
                    if (success) {
                        if (flag == 9) {
                            mPasswordView.setError(nombre);
                            mPasswordView.requestFocus();
                            break;
                        }
                        if (generarArchivo(constantes.neoclinicaConfig, matricula, profesional)) {
                            Log.i("JONATT", "GENERO BIEN LA CONFIG");
                            estadoArchivo = constantes.CONFIG_FOUND;
                            guardarMatricula();
                        } else {
                            Log.i("JONATT", "NO SE GENERO LA CONFIG");
                        }

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("nombre", nombre);
                        i.putExtra("matricula", mMatricula);
                        i.putExtra("password", mPassword);
                        i.putExtra("profesional", profesional);
                        i.putExtra("cookie", cookie);
                        startActivityForResult(i,constantes.RESULT_MAIN_ACTIVITY);

                    } else {
                        if (status == 99) {
                            mPasswordView.setError(getString(R.string.servidor_timeout));
                            mPasswordView.requestFocus();
                            break;
                        } else {
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                        }
                    }
                    break;
                case 9:
                    if (status == 99 || status == 404  || status == 405) {
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
            userLoginTaskTask = null;
            showProgress(false);
        }
    }


}








//crea acceso directo
    /*
    public void createShortCut(){
        // a Intent to create a shortCut
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //repeat to create is forbidden
        shortcutintent.putExtra("duplicate", false);
        //set the name of shortCut
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        //set icon
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //set the application to lunch when you click the icon
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext() , LoginActivity.class));
        //sendBroadcast,done
        sendBroadcast(shortcutintent);
    }
    */