package hsfarmacia.farmaclub;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private String extStorageDirectory = null;

    private TextView tvUsuario;
    private TextView tvTarjeta;
    private EditText edtUsuario;
    private EditText edtPassword;
    private EditText edtPasswordRep;
    private TextView tvTerminos;
    private Button btnCancelar;
    private Button btnAceptar;
    private CheckBox cbTerminos;

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 3;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 4;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 5;


    private static final int REQUEST_RESULT = 333;
    private static final int MY_NOTIFICATION = 444;



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
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        cbTerminos = (CheckBox) findViewById(R.id.cbTerminos);

        btnAceptar.setEnabled(false);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
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
                if ((id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) && cbTerminos.isChecked()) {
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
                //tvTerminos.setTextColor(Color.RED);
                tvTerminos.setEnabled(false);
                tvTerminos.setText(getString(R.string.tvTerminosDownload));
                conditionsTask = new ConditionsTask();
                conditionsTask.execute((Void) null);
            }
        });

        cbTerminos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbTerminos.isChecked()) {
                    // your code to checked checkbox
                    btnAceptar.setEnabled(true);
                } else {
                // your code to  no checked checkbox
                    btnAceptar.setEnabled(false);
                }
            }
        });

        verificarPermisos();
    }

    private void verificarPermisos(){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
            return;
        }

        int permsRequestCode = 100;
        String[] perms = {android.Manifest.permission.INTERNET,
                          android.Manifest.permission.ACCESS_NETWORK_STATE,
                          android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                          android.Manifest.permission.READ_PHONE_STATE,
                          android.Manifest.permission.READ_EXTERNAL_STORAGE};
        int internetPermission = checkSelfPermission(android.Manifest.permission.INTERNET);
        int accessNetworkStatePermission = checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
        int writeExternalStoragePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPhoneStatePermission = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE);
        int readExternalStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (internetPermission != android.content.pm.PackageManager.PERMISSION_GRANTED ||
            accessNetworkStatePermission != android.content.pm.PackageManager.PERMISSION_GRANTED ||
            writeExternalStoragePermission != android.content.pm.PackageManager.PERMISSION_GRANTED ||
            readPhoneStatePermission != android.content.pm.PackageManager.PERMISSION_GRANTED ||
            readExternalStoragePermission != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
             requestPermissions(perms, permsRequestCode);
        }
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

        if (password.length() < 4 || password.length() > 15) {
            edtPassword.setError(getString(R.string.error_invalid_password));
            focusView = edtPassword;
            cancel = true;
        }

        if (passwordRep.length() < 4 || passwordRep.length() > 15) {
            edtPasswordRep.setError(getString(R.string.error_invalid_password));
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

    //eliminar metodo
    private void notificar(){
        NotificationManager mNotifyManager;
        Notification.Builder mBuilder;
        String filePath = extStorageDirectory + "/" + constantes.pdfName;
        File file = new File(filePath);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = file.getName().substring(file.getName().indexOf(".")+1);
        ext= "pdf";// file.getName().substring(file.getName().indexOf(".")+1);
        String type = mime.getMimeTypeFromExtension(ext);

        Intent openFile;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            openFile = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
            openFile.setDataAndType(Uri.fromFile(file), type);
        } else {
            openFile = new Intent(Intent.ACTION_VIEW, Uri.parse(filePath));
            openFile.setDataAndType(Uri.parse(filePath), type);
        }


        openFile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent p = PendingIntent.getActivity(this, 0, openFile, 0);
        mNotifyManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle("Download")
                .setContentText("Download in progress")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(p);
        //mBuilder.setProgress(100, 0, false);
        mNotifyManager.notify(MY_NOTIFICATION, mBuilder.build());





/*
        Intent intent= new Intent(getBaseContext(), PdfActivity.class);
        intent.putExtra("idNotification", MY_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), REQUEST_RESULT, intent, PendingIntent.FLAG_ONE_SHOT);


        Notification.Builder builder = new Notification.Builder(getBaseContext())
                .setContentTitle("un titulo peola")
                .setContentText("descripcion peola")
                .setSmallIcon(android.R.drawable.ic_menu_info_details);

        builder.addAction(android.R.drawable.ic_menu_share, "tutto ok",pendingIntent);


        issueNotification(builder);

*/




/* ANDA
        //What happen when you will click on button
        Intent intent = new Intent(this, PdfActivity.class);
        intent.putExtra("idNotification", MY_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        //Button
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Go", pendingIntent).build();

        //Notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Back to Application ?")
                .setContentTitle("Amazing news")
                .addAction(action) //add buton
                .build();

        //Send notification
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
ANDA
*/









        /*
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                int segundos = 5+r.nextInt(11);
                try {
                    Thread.currentThread().sleep(1000*segundos);
                    //__INVOCAR METODO NOTIFICAR que realizará en el PASO 3
                    notificarEnvio("lopez puto");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t =new Thread(r);
        t.start();
        */
    }

    //eliminar metodo
    private void issueNotification(Notification.Builder builder) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION, builder.build());
    }

    //eliminar metodo
    private void notificarEnvio(String destinatario){
// Create an explicit intent for an Activity in your app
        Intent intent = new Intent(getBaseContext(), PdfActivity.class);
        intent.putExtra("pdfPath", extStorageDirectory + "/" + constantes.pdfName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent,
                0);
        Log.d("APP_MY_RESTO","Creando notificacion");
        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(getBaseContext(), "1")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle("Pedido entregado")
                .setContentText("El pedido para "+destinatario+" ha sido entregado")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
// Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getBaseContext());
        notificationManager.notify(100, mBuilder.build());
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
                case 0:
                    edtUsuario.setError(getString(R.string.usuario_existente));
                    edtUsuario.requestFocus();
                    break;
                case 1:
                    if (success) {
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
        private int flagLeePDF = 0;

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
                        flagLeePDF = 1;
                    }
                    fileOutputStream.close();
                    inputStream.close();
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
            //tvTerminos.setTextColor(Color.RED);
            tvTerminos.setEnabled(true);
            tvTerminos.setText(getString(R.string.tvTerminos));
            tvTerminos.setPaintFlags(tvTerminos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            if (success && flagLeePDF == 1) {
                Intent intent = new Intent(getBaseContext(), PdfActivity.class);
                intent.putExtra("pdfPath", extStorageDirectory + "/" + constantes.pdfName);
                startActivity(intent);

                //notificar();




                /*
                File pdfFile = new File(extStorageDirectory + "/" + constantes.pdfName);
                Uri path = null;

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M){
                    path = Uri.fromFile(pdfFile);
                } else {
                    path = Uri.parse(pdfFile.getPath());
                }

                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                //pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Log.i("PDF", "No Application available to view PDF");
                    Log.i("PDF", e.getMessage());
                    Toast.makeText(getBaseContext(),"No existe aplicacion para visualizar PDF.", Toast.LENGTH_SHORT).show();

                }
                */
            } else {
                Toast.makeText(getBaseContext(),"Problemas en la descarga de la Licencia.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
