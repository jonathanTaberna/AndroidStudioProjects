package hsfarmacia.farmaclub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import hsfarmacia.farmaclub.constantes.constantes;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private int status = 0;
    private UserLoginTask mAuthTask = null;
    private ResetPassTask resetPassTask = null;

    // UI references.
    private AutoCompleteTextView mUusarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox cbRecordarUsuario;
    private Button mIngresarButton;
    private TextView tvTengoUsuario;
    private TextView tvOlvideUsuario;
    private TextInputLayout tilPassword;


    private String estadoArchivo = "";
    private String tarjeta = "";
    private int flagVoyConUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        flagVoyConUsuario = 0;
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

        mIngresarButton = (Button) findViewById(R.id.btnIngresar);
        mIngresarButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estadoArchivo == constantes.CONFIG_FOUND) {
                    guardarUsuario();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mUusarioView.getWindowToken(), 0);
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tilPassword = findViewById(R.id.tilPassword);
        cbRecordarUsuario = findViewById(R.id.cbRecordarUsuario);
        tvTengoUsuario = findViewById(R.id.tvTengoUsuario);
        tvTengoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUusarioView.setHint(R.string.edtUsuarioU);
                cbRecordarUsuario.setVisibility(View.VISIBLE);
                mUusarioView.setInputType(InputType.TYPE_CLASS_TEXT);
                cbRecordarUsuario.setChecked(true);
                tvTengoUsuario.setVisibility(View.INVISIBLE);
                flagVoyConUsuario = 1;
            }
        });

        tvOlvideUsuario = findViewById(R.id.tvOlvideUsuario);
        tvOlvideUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estadoArchivo = constantes.CONFIG_NOT_FOUND;
                mUusarioView.setText("");
                mUusarioView.setHint(R.string.edtUsuarioT);
                mUusarioView.setInputType(InputType.TYPE_CLASS_NUMBER);

                mIngresarButton.setText(R.string.btnReestablecer);
                mPasswordView.setVisibility(View.INVISIBLE);
                cbRecordarUsuario.setVisibility(View.INVISIBLE);
                tvTengoUsuario.setVisibility(View.INVISIBLE);
                tvOlvideUsuario.setVisibility(View.INVISIBLE);
                tilPassword.setVisibility(View.INVISIBLE);
                flagVoyConUsuario = 2;

            }
        });



        File archivo = new File(getBaseContext().getFilesDir()+ "/" + constantes.farmaclubConfig);
        if (archivo.exists()) {
            estadoArchivo = constantes.CONFIG_FOUND;
            mUusarioView.setHint(R.string.edtUsuarioU);
            cbRecordarUsuario.setVisibility(View.VISIBLE);
            mUusarioView.setInputType(InputType.TYPE_CLASS_TEXT);
            cbRecordarUsuario.setChecked(true);
            String usuarioCargado = leerUsuarioCargado();
            if (!usuarioCargado.isEmpty()) {
                mUusarioView.setText(usuarioCargado);
                mPasswordView.requestFocus();
            }
            tvTengoUsuario.setVisibility(View.INVISIBLE);
            tvOlvideUsuario.setVisibility(View.VISIBLE);
        } else  {
            estadoArchivo = constantes.CONFIG_NOT_FOUND;
            mUusarioView.setHint(R.string.edtUsuarioT);
            cbRecordarUsuario.setVisibility(View.INVISIBLE);
            mUusarioView.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvTengoUsuario.setVisibility(View.VISIBLE);
            tvOlvideUsuario.setVisibility(View.INVISIBLE);
    }

        /*
        if (estadoArchivo == CONFIG_FOUND){
            mUusarioView.setHint(R.string.edtUsuarioU);
            mUusarioView.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            mUusarioView.setHint(R.string.edtUsuarioT);
            mUusarioView.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        */
    }

    private void guardarUsuario() {

        try {
            FileInputStream fis = getBaseContext().openFileInput(constantes.farmaclubConfig);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("usuario_utilizado")) {
                    if (cbRecordarUsuario.isChecked()) {
                        line = "usuario_utilizado:" + mUusarioView.getText().toString();
                    } else {
                        line = "usuario_utilizado:";
                    }
                }
                sb.append(line + "\n");
            }
            fis.close();
            isr.close();
            bufferedReader.close();

            FileOutputStream mOutput = openFileOutput(constantes.farmaclubConfig, Context.MODE_PRIVATE);
            mOutput.write(sb.toString().getBytes());
            mOutput.flush();
            mOutput.close();


        } catch (Exception e) {
            Log.i("FILE-ERROR: ", e.getMessage());
        }





/*


        List<String> lines = new ArrayList<String>();
        String line = null;

            try {
                File f1 = new File(farmaclubConfig);
                FileReader fr = new FileReader(f1);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {
                    if (line.contains("usuario_utilizado")) {
                        if (cbRecordarUsuario.isChecked()) {
                            line = "usuario_utilizado: " + mUusarioView.getText().toString();
                        } else {
                            line = "usuario_utilizado: ";
                        }
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

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("FILE-ERROR: ", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("FILE-ERROR: ", e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.i("FILE-ERROR: ", ex.getMessage());
            }
*/
    }


    private String leerUsuarioCargado(){
        String usuario = "";
        try {
            FileInputStream mInput = openFileInput(constantes.farmaclubConfig);
            String[] partes = null;

            InputStreamReader isr = new InputStreamReader (mInput) ;
            BufferedReader buffreader = new BufferedReader (isr) ;
            String linea = buffreader.readLine();
            while ( linea != null ) {
                if (linea.contains("usuario_utilizado:")) {
                    partes = linea.split("usuario_utilizado:");
                    if (partes.length >= 2) {
                        String parte1 = partes[0];
                        String parte2 = partes[1];
                        usuario = partes[1];
                    }
                    //mUusarioView.setText(parte2);
                }
                linea = buffreader.readLine(); //lee proxima fila
            }
            mInput.close();
        }
        catch (Exception e) {
            Log.i("CONFIG-NOT-FOUND", e.getMessage());
        }
        return  usuario;
    }

    private boolean generarArchivo(String archivo, String tarjeta) {
        boolean retorno = false;
        try {
            FileOutputStream mOutput = openFileOutput(archivo, Context.MODE_PRIVATE);
            mOutput.write(("nro_tarjeta:" + tarjeta + "\n").getBytes());
            mOutput.write(("usuario_utilizado:").getBytes());
            mOutput.flush();
            mOutput.close();
            //mUusarioView.setText("");


            /*
            FileWriter fw = new FileWriter( archivo);
            BufferedWriter out = new BufferedWriter(fw);
            out.write("nro_tarjeta:" + mUusarioView.getText().toString());
            out.write("usuario_utilizado:");
            out.flush();
            out.close();
            */


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
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password) && flagVoyConUsuario != 2) {
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

        if (usuario.contains(" ")) {
            mUusarioView.setError(getString(R.string.usuario_invalido));
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

            if (flagVoyConUsuario == 2) {
                //Toast.makeText(getBaseContext(), R.string.toastOlvideUsuario, Toast.LENGTH_LONG).show();
                tarjeta = usuario;

                resetPassTask = new ResetPassTask();
                resetPassTask.execute((Void) null);

            } else {

                mAuthTask = new UserLoginTask(usuario, password);
                mAuthTask.execute((Void) null);
            }
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() >= 4 && password.length() < 15;
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
                mUusarioView.setText(data.getStringExtra("usuario"));
                mUusarioView.setInputType(InputType.TYPE_CLASS_TEXT);
                mPasswordView.setText("");
                if (generarArchivo(constantes.farmaclubConfig, tarjeta)){
                    Log.i("JONATT", "GENERO BIEN LA CONFIG");
                    estadoArchivo = constantes.CONFIG_FOUND;
                    cbRecordarUsuario.setVisibility(View.VISIBLE);
                    cbRecordarUsuario.setChecked(true);
                    mPasswordView.requestFocus();
                } else {
                    Log.i("JONATT", "NO SE GENERO LA CONFIG");
                }
            }
            //else {
            //    Log.i("RESULT", "RESULT_NUEVO_USUARIO FAIL");
            //    mUusarioView.setText("pepito fuma marihuana");
            //}
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsuario;
        private final String mPassword;
        private JSONObject jsonResp = null;

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
                if (estadoArchivo == constantes.CONFIG_NOT_FOUND && flagVoyConUsuario == 0){
                    //url = new URL("http://192.168.2.50:8080/farmaclubserver/rest/user/validaIni"); //in the real code, there is an ip and a port
                    url = new URL( constantes.pathConnection + "validaIni"); //in the real code, there is an ip and a port
                } else {
                    //url = new URL("http://192.168.2.50:8080/farmaclubserver/rest/user/valida"); //in the real code, there is an ip and a port
                    url = new URL(constantes.pathConnection +"valida"); //in the real code, there is an ip and a port
                }

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                if (estadoArchivo == constantes.CONFIG_NOT_FOUND && flagVoyConUsuario == 0){
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
                        if (estadoArchivo == constantes.CONFIG_NOT_FOUND && flagVoyConUsuario == 0) {
                            //Intent nuevoUsuario = new Intent(getApplicationContext(), UsuarioActivity.class);
                            Intent nuevoUsuario = new Intent(getBaseContext(), UsuarioActivity.class);
                            nuevoUsuario.putExtra("tarjeta", tarjeta);
                            nuevoUsuario.putExtra("nombre",nombre);
                            startActivityForResult(nuevoUsuario, constantes.RESULT_NUEVO_USUARIO);
                        } else {
                            if (flagVoyConUsuario == 1) {
                                if (generarArchivo(constantes.farmaclubConfig, tarjeta)) {
                                    Log.i("JONATT", "GENERO BIEN LA CONFIG");
                                    estadoArchivo = constantes.CONFIG_FOUND;
                                    guardarUsuario();
                                } else {
                                    Log.i("JONATT", "NO SE GENERO LA CONFIG");
                                }
                            }

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


    public class ResetPassTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject respuestaResetPass = new JSONObject();
        private int respuestaStatus = 0;
        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + "reset_pass");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("tarjeta", tarjeta);

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

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");
                        respuestaResetPass = new JSONObject(inputLine);
                    }
                    JsonResponse = buffer.toString();
                    Log.i("RESPONSE",JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    respuestaStatus = 99;
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
            resetPassTask = null;
            showProgress(false);

            //variables de respuesta
            int salida = 9;
            String msj = "";

            try {
                salida = respuestaResetPass.getInt("salida");
                msj = respuestaResetPass.getString("msj");
            } catch (Exception e){
                salida = 9;
                mUusarioView.setError(getString(R.string.error_json));
                mUusarioView.requestFocus();
                return;
            }

            switch (salida) {
                case 1:
                    if (success) {
                        //Toast.makeText(getBaseContext(), R.string.toastErrorReestablecerUsuario, Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), R.string.toastOlvideUsuario, Toast.LENGTH_LONG).show();

                        mIngresarButton.setText(R.string.btnIngresar);
                        tilPassword.setVisibility(View.VISIBLE);
                        mPasswordView.setVisibility(View.VISIBLE);
                        tvTengoUsuario.setVisibility(View.INVISIBLE);
                        tvOlvideUsuario.setVisibility(View.INVISIBLE);
                        flagVoyConUsuario = 0;

                    } else {
                        mUusarioView.setError(getString(R.string.toastErrorReestablecerUsuario));
                        mUusarioView.requestFocus();
                    }
                    break;
                case 2:
                case 3:
                case 4:
                    mUusarioView.setError(msj);
                    mUusarioView.requestFocus();
                    break;
                case 9:
                    if (respuestaStatus == 99) {
                        mUusarioView.setError(getString(R.string.servidor_timeout));
                        mUusarioView.requestFocus();
                    } else {
                        mUusarioView.setError(getString(R.string.toastErrorReestablecerUsuario));
                        mUusarioView.requestFocus();
                    }
                    break;
                default:
                    mUusarioView.setError(getString(R.string.toastErrorReestablecerUsuario));
                    mUusarioView.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            resetPassTask = null;
            showProgress(false);
        }
    }

}

