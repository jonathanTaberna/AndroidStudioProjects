package hsfarmacia.farmaclub;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import hsfarmacia.farmaclub.constantes.constantes;

public class UsuarioActivity extends AppCompatActivity {

    private String tarjeta;
    private String nombre;
    private String usuario;
    private int status = 0;
    private JSONObject jsonResp = null;
    private UserRegisterTask mAuthTask = null;

    private TextView tvUsuario;
    private TextView tvTarjeta;
    private EditText edtUsuario;
    private EditText edtPassword;
    private EditText edtPasswordRep;
    private Button btnCancelar;
    private Button btnAceptar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        Bundle extras = getIntent().getExtras();
        tarjeta = extras.getString("tarjeta");
        nombre = extras.getString("nombre");

        tvUsuario = (TextView) findViewById(R.id.tvUsuarioUsuario);
        tvUsuario.setText("Bienvenido "+ nombre);
        tvTarjeta = (TextView) findViewById(R.id.tvUsuarioTarjeta);
        tvTarjeta.setText("Su Numero de tarjeta es "+tarjeta);
        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPasswordRep = (EditText) findViewById(R.id.edtPasswordRep);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()){
                    mAuthTask = new UserRegisterTask(usuario, tarjeta);
                    mAuthTask.execute((Void) null);
                }
            }
        });


        edtPasswordRep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    if (validarCampos()){
                        mAuthTask = new UserRegisterTask(usuario, tarjeta);
                        mAuthTask.execute((Void) null);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private boolean validarCampos(){
        usuario = edtUsuario.getText().toString();
        String password = edtPassword.getText().toString();
        String passwordRep = edtPasswordRep.getText().toString();

        edtUsuario.setError(null);
        edtPassword.setError(null);
        edtPasswordRep.setError(null);
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(usuario))  {
            edtUsuario.setError(getString(R.string.error_field_required));
            focusView = edtUsuario;
            cancel = true;
        }

        if (TextUtils.isEmpty(password))  {
            edtPassword.setError(getString(R.string.error_field_required));
            focusView = edtPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(passwordRep))  {
            edtPasswordRep.setError(getString(R.string.error_field_required));
            focusView = edtPasswordRep;
            cancel = true;
        }

        if (usuario.contains(" ")) {
            edtUsuario.setError(getString(R.string.usuario_invalido));
            focusView = edtUsuario;
            cancel = true;
        }

        if (!password.equals(passwordRep)) {
            edtPasswordRep.setError(getString(R.string.passwords_diferentes));
            focusView = edtPasswordRep;
            cancel = true;
        }

        if (mAuthTask != null) {
            cancel = true;
        }

        if (cancel) {
            return false;
        } else {
            return true;
        }
    }




    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsuario;
        private final String mTarjeta;

        UserRegisterTask(String usuario, String tarjeta) {
            mUsuario = usuario;
            mTarjeta = tarjeta;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                URL url = new URL(constantes.pathConnection + "existeusu"); //in the real code, there is an ip and a port
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                obj.put("tarjeta", mTarjeta);
                obj.put("usuario", mUsuario);

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

            int salida = 9;

            try {
                salida = jsonResp.getInt("salida");
            } catch (Exception e){
                salida = 9;
            }

            switch (salida) {
                case 1:
                    if (success) {
                        if (actualizaUsuario()) {
                            Intent data = new Intent();
                            data.putExtra("usuario", usuario);
                            setResult(RESULT_OK, data);
                            finish();
                        } else {
                            edtUsuario.setError(getString(R.string.error_update));
                            edtUsuario.requestFocus();
                        }
                    } else {
                        edtUsuario.setError(getString(R.string.error_json));
                        edtUsuario.requestFocus();
                    }
                    break;
                case 9:
                    if (status == 99) {
                        edtUsuario.setError(getString(R.string.servidor_timeout));
                        edtUsuario.requestFocus();
                    } else {
                        edtUsuario.setError(getString(R.string.usuario_existente));
                        edtUsuario.requestFocus();
                    }
                    break;
                default:
                    edtPassword.setError(getString(R.string.error_incorrect_password));
                    edtPassword.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        // quede aca... sale error por la accion en el MainThread
        private boolean actualizaUsuario() {

            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL(constantes.pathConnection + "actualiza"); //in the real code, there is an ip and a port
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                obj.put("tarjeta", mTarjeta);
                obj.put("usuario", mUsuario);
                obj.put("pass", edtPassword.getText().toString());

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


    }
}
