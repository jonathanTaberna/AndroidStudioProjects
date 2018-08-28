package hsfarmacia.farmaclub;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
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
import java.io.File;
import java.io.FileOutputStream;
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
    private UserRegisterTask userRegisterTask = null;
    private UpdateUserTask updateUserTask = null;
    private ConditionsTask conditionsTask = null;

    private TextView tvUsuario;
    private TextView tvTarjeta;
    private EditText edtUsuario;
    private EditText edtPassword;
    private EditText edtPasswordRep;
    private TextView tvTerminos;
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
        tvTerminos = (TextView) findViewById(R.id.tvTerminos);
        //tvTerminos.setPaintFlags(tvTerminos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                    userRegisterTask = new UserRegisterTask(usuario, tarjeta);
                    userRegisterTask.execute((Void) null);
                }
            }
        });
        edtPasswordRep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    if (validarCampos()){
                        userRegisterTask = new UserRegisterTask(usuario, tarjeta);
                        userRegisterTask.execute((Void) null);
                        return true;
                    }
                }
                return false;
            }
        });
        tvTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tvTerminos","hizo click");
                tvTerminos.setTextColor(Color.RED);
                tvTerminos.setEnabled(false);
                tvTerminos.setText(getString(R.string.tvTerminosDownload));
                conditionsTask = new ConditionsTask();
                conditionsTask.execute((Void) null);
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

        if (userRegisterTask != null) {
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
                conn.setRequestProperty("Accept", "application/json");
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
                Log.i("MSG", conn.getResponseMessage());

                if (status == 200) { //respuesta OK
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");
                        jsonResp = new JSONObject(inputLine);
                    }
                    JsonResponse = buffer.toString();
                    Log.i("RESPONSE", JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    status = 99;
                }
            } catch (SocketTimeoutException e) {
                status = 99;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
            userRegisterTask = null;

            int salida = 9;

            try {
                salida = jsonResp.getInt("salida");
            } catch (Exception e) {
                salida = 9;
            }

            switch (salida) {
                case 1:
                    if (success) {
                        /*
                        if (actualizaUsuario()) {
                            Intent data = new Intent();
                            data.putExtra("usuario", usuario);
                            setResult(RESULT_OK, data);
                            finish();
                        } else {
                            edtUsuario.setError(getString(R.string.error_update));
                            edtUsuario.requestFocus();
                        }
                        */
                        updateUserTask = new UpdateUserTask(usuario,edtPassword.getText().toString(),tarjeta);
                        updateUserTask.execute((Void) null);
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
            userRegisterTask = null;
        }
    }


    /**
     * Represents an asynchronous update task used to update the user.
     */
    public class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsuario;
        private final String mTarjeta;
        private final String mPassword;
        private JSONObject respUpdate;

        UpdateUserTask(String usuario, String password, String tarjeta) {
            mUsuario = usuario;
            mTarjeta = tarjeta;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                URL url = new URL(constantes.pathConnection + "actualiza"); //in the real code, there is an ip and a port
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();
                obj.put("tarjeta", mTarjeta);
                obj.put("usuario", mUsuario);
                obj.put("pass", mPassword);

                Log.i("JSON", obj.toString());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(obj.toString());

                os.flush();
                os.close();

                status = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                if (status == 200) { //respuesta OK
                    InputStream inputStream = conn.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine + "\n");
                        respUpdate = new JSONObject(inputLine);
                    }
                    JsonResponse = buffer.toString();
                    Log.i("RESPONSE", JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    status = 99;
                }
            } catch (SocketTimeoutException e) {
                status = 99;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
            updateUserTask = null;

            int salida = 9;

            try {
                salida = respUpdate.getInt("salida");
            } catch (Exception e) {
                salida = 9;
            }

            switch (salida) {
                case 1:
                    if (success) {
                        Intent data = new Intent();
                        data.putExtra("usuario", usuario);
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        edtUsuario.setError(getString(R.string.error_update));
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
            updateUserTask = null;
        }
    }














    /**
     * Represents an asynchronous task used to read the conditions.
     */
    public class ConditionsTask extends AsyncTask<Void, Void, Boolean> {
/*
        private final String mUsuario;
        private final String mPassword;
        private JSONObject respuestaPDF = new JSONObject();
        private PdfDocument pdfDocument = null;

*/
        private int statusPDF = 0;
        private static final int  MEGABYTE = 1024 * 1024;
        private String extStorageDirectory = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL( constantes.pathConnection + "licencia");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20000); //20 segundos
                conn.connect();

                extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "licence");
                folder.mkdir();

                File pdfFile = new File(folder, constantes.pdfName);
                pdfFile.createNewFile();

                statusPDF = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                if (statusPDF == 200){ //respuesta OK

                    InputStream inputStream = conn.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(extStorageDirectory + "/" + constantes.pdfName);
                    int totalSize = conn.getContentLength();

                    byte[] buffer = new byte[MEGABYTE];
                    int bufferLength = 0;
                    while((bufferLength = inputStream.read(buffer))>0 ){
                        fileOutputStream.write(buffer, 0, bufferLength);
                    }
                    fileOutputStream.close();
                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    statusPDF = 99;
                }
            }catch (SocketTimeoutException e) {
                statusPDF = 99;
            } catch (Exception e){
                e.printStackTrace();
            }  finally {
                conn.disconnect();
            }

            if (statusPDF == 200) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                tvTerminos.setTextColor(Color.BLACK);
                tvTerminos.setEnabled(true);
                tvTerminos.setText(getString(R.string.tvTerminos));
                File pdfFile = new File(extStorageDirectory + "/" + constantes.pdfName);
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Log.i("PDF", "No Application available to view PDF");
                }
            }
        }

    }








}