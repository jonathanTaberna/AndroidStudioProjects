package jt.cotitagliero;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import jt.cotitagliero.constantes.constantes;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private UserLoginTask mAuthTask = null;
    private int status = 0;

    private AutoCompleteTextView mUusarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox cbRecordarUsuario;
    private Button mIngresarButton;

    private String estadoArchivo = "";
    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contexto = this;

        mUusarioView = (AutoCompleteTextView) findViewById(R.id.edtUsuario);
        mPasswordView = (EditText) findViewById(R.id.edtPassword);
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
        cbRecordarUsuario = findViewById(R.id.cbRecordarUsuario);
        mProgressView = findViewById(R.id.login_progress);

        showProgress(false);

        File archivo = new File(getBaseContext().getFilesDir()+ "/" + constantes.cotiTaglieroConfig);
        if (archivo.exists()) {
            estadoArchivo = constantes.CONFIG_FOUND;
            cbRecordarUsuario.setChecked(true);
            String usuarioCargado = leerUsuarioCargado();
            if (!usuarioCargado.isEmpty()) {
                mUusarioView.setText(usuarioCargado);
                mPasswordView.requestFocus();
            }
        } else  {
            estadoArchivo = constantes.CONFIG_NOT_FOUND;
        }
    }

    private void guardarUsuario() {

        try {
            FileInputStream fis = getBaseContext().openFileInput(constantes.cotiTaglieroConfig);
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

            FileOutputStream mOutput = openFileOutput(constantes.cotiTaglieroConfig, Context.MODE_PRIVATE);
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
            FileInputStream mInput = openFileInput(constantes.cotiTaglieroConfig);
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

    private boolean generarArchivo(String archivo, String usuario) {
        boolean retorno = false;
        try {
            FileOutputStream mOutput = openFileOutput(archivo, Context.MODE_PRIVATE);
            mOutput.write(("usuario_utilizado:" + usuario).getBytes());
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


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
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

            showProgress(true);

            mAuthTask = new UserLoginTask(usuario, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
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
                URL url = new URL( constantes.cotiPaciente );
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("metodo", "getPacienteByUserPass");
                obj.put("user", mUsuario);
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
                        if (inputLine.equals("null")) {
                            Toast.makeText(contexto, "JSON Null", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            jsonResp = new JSONObject(inputLine);
                        }
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
            mAuthTask = null;
            showProgress(false);

            int salida = 9;
            int cantidad = 0;
            String id = "";
            String nombre = "";
            String fecha_nac = "";
            String tel = "";
            String dni = "";
            JSONArray jsonObject = null;

            try {
                cantidad = jsonResp.getInt("cantidad");
                if (cantidad > 0) {
                    jsonObject = jsonResp.getJSONArray("items");
                    id = jsonObject.getJSONObject(0).getString("id").trim();
                    nombre = jsonObject.getJSONObject(0).getString("nombre").trim();
                    fecha_nac = jsonObject.getJSONObject(0).getString("fecha_nac").trim();
                    tel = jsonObject.getJSONObject(0).getString("tel").trim();
                    dni = jsonObject.getJSONObject(0).getString("dni").trim();
                    salida = 1;
                } else {
                    salida = 7;
                }
            } catch (Exception e){
                salida = 8;
            }

            switch (salida) {
                case 1:
                case 7:
                case 8:
                    if (salida == 8) {
                        Log.i("Login", "error en casteo JSON");
                        if (status == 98) {
                            mPasswordView.setError(getString(R.string.servidor_timeout));
                            mPasswordView.requestFocus();
                            break;
                        }
                    }
                    if (salida == 7) {
                        Log.i("Login", "usuario o pass incorrectas");
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                        break;
                    }
                    if (success) {
                        if (generarArchivo(constantes.cotiTaglieroConfig, mUsuario)) {
                            Log.i("JONATT", "EXITO CON EL JSON PUTO");
                            // if si cliente
                            Intent i = new Intent(getApplicationContext(), ClienteActivity.class);
                            i.putExtra("id", id);
                            startActivity(i);
                            // if si admin
                        }
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
            mAuthTask = null;
            showProgress(false);
        }
    }
}

