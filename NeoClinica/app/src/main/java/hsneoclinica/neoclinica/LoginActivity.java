package hsneoclinica.neoclinica;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
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
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import hsneoclinica.neoclinica.constantes.constantes;
import hsneoclinica.neoclinica.geolocalizacion.GeoLocalizacionActivity;
import hsneoclinica.neoclinica.provisorios.Turno;

import static hsneoclinica.neoclinica.constantes.constantes.RESULT_CERRAR_SESION;
import static hsneoclinica.neoclinica.constantes.constantes.RESULT_HS_ACTIVITY;
import static hsneoclinica.neoclinica.constantes.constantes.neoServer;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,
                                                                ActivityCompat.OnRequestPermissionsResultCallback {

    private int status = 0;
    private UserLoginTask userLoginTask = null;
    private ValidaIniTask validaIniTask = null;
    private HsLoginTask hsLoginTask = null;
    //private GeolocalizacionTask geolocalizacionTask = null;
    //private FetchCordinates fetchCordinates = null;
    private String cookie = "";

    // UI references.
    private AutoCompleteTextView mMatriculaView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox cbRecordarMatricula;
    private Button mIngresarButton;
    private TextInputLayout tilPassword;
    private ImageView toolbarLogo;

    private String empresa = "";
    private String nombre = "";
    private String nombreEmpresa = "";
    private String estadoArchivo = "";
    private String matricula = "";
    private String password = "";
    private String profesional = "";
    private JSONArray profesionales = null;

    private double latitud;
    private double longitud;
    private double precision;
    //private String[] datos;
    /*Se declara la clase encargada de proporcionar acceso al servicio
    de localizacion del sistema.*/
    private LocationManager locManager;
    /*Interfaz encargada de recibir las notificaciones del LocationManager
    cuando se cambia la localizacion.*/
    private LocationListener locListener;
    /*Se declara una variable de tipo Location que accedera a la ultima posicion conocida proporcionada por el proveedor.*/
    //private Location loc;
    private  Boolean precisionCorrecta = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //createShortCut();

        toolbarLogo = (ImageView) findViewById(R.id.toolbar_logo);
        Bundle extras = getIntent().getExtras();
        empresa = extras.getString("empresa");
        switch (empresa){
            case "SanLucas":
                constantes.pathConnection = constantes.sanLucasServer;
                toolbarLogo.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "SanatorioPrivado":
                constantes.pathConnection = constantes.sanatorioPrivadoServer;
                toolbarLogo.setImageResource(R.drawable.sanatorio_privado_logo);
                break;
            case "Neoclinica":
                constantes.pathConnection = constantes.neoServer;
                toolbarLogo.setImageResource(R.drawable.neoclinica_logo);
                break;
            case "Odontograssi":
                constantes.pathConnection = constantes.odontograssiServer;
                toolbarLogo.setImageResource(R.drawable.odontograssi_logo);
                break;
            case "ResonanciaR4":
                constantes.pathConnection = constantes.resonanciaR4Server;
                toolbarLogo.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "Urologico":
                constantes.pathConnection = constantes.urologicoServer;
                toolbarLogo.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "ClinicaPrivGralDeheza":
                constantes.pathConnection = constantes.clinicaPrivadaGralDehezaServer;
                toolbarLogo.setImageResource(R.drawable.clinica_gral_deheza_logo);
                break;
            case "HospitalComGralDeheza":
                constantes.pathConnection = constantes.hospitalGralDehezaServer;
                toolbarLogo.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "HS":
                constantes.pathConnection = constantes.hsServer;
                break;
            default:
                break;
        }

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
            String matriculaCargada = leerMatriculaCargada();
            if (!matriculaCargada.isEmpty()) {
                mMatriculaView.setText(matriculaCargada);
                mPasswordView.requestFocus();
            }
        } else  {
            estadoArchivo = constantes.CONFIG_NOT_FOUND;
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
                }
                if (linea.contains("matricula_utilizada:")) {
                    partes = linea.split("matricula_utilizada:");
                    if (partes.length >= 2) {
                        String parte1 = partes[0];
                        String parte2 = partes[1];
                        matricula = partes[1];
                    }
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

        matricula = mMatriculaView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();

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

            if (constantes.activarGeoLocalizacion) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                //while (precisionCorrecta == false) {
                    // // //capturarGeoLocalizacion();

                //}
                //if (precisionCorrecta) {
                //    validaIniTask = new ValidaIniTask(matricula, password);
                //    validaIniTask.execute((Void) null);
                //}
            } else {
                validaIniTask = new ValidaIniTask(matricula, password);
                validaIniTask.execute((Void) null);
            }
            //mAuthTask = new UserLoginTask(matricula, password);
            //mAuthTask.execute((Void) null);
        }
    }

    /*
    private void capturarGeoLocalizacion(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        //Intent i = new Intent(getApplicationContext(), GeoLocalizacionActivity.class);
        //startActivityForResult(i, constantes.RESULT_GEOLOCALIZACION);

        //rastreoGPS();
        //if (precisionCorrecta) {return;}
    }
    */

    /*Metodo encargado de actualizar la posicion del dispositivo
    GPS cuando este cambie de localizacion.*/
    private void rastreoGPS() {
        /*Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.*/
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Se define la interfaz LocationListener, que debera implementarse con los siguientes metodos.
        locListener = new LocationListener() {
            //Metodo que sera llamado cuando cambie la localizacion.
            @Override
            public void onLocationChanged(Location location) {
                capturarPosicion(location);
                if (precisionCorrecta) {return;}
            }

            //Metodo que sera llamado cuando se produzcan cambios en el estado del proveedor.
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            //Metodo que sera llamado cuando el proveedor este habilitado para el usuario.
            @Override
            public void onProviderEnabled(String provider)
            {
            }

            //Metodo que sera llamado cuando el proveedor este deshabilitado para el usuario.
            @Override
            public void onProviderDisabled(String provider)
            {
            }
        };
        /*Se llama al metodo encargado establecer la localizacion actualizada,
        recibiendo como parametros de entrada el nombre del proveedor, el intervalo de tiempo entre cada
        actualizacion, distancia en metros entre localizaciones actualizadas, y la variable de tipo LocationListener
        que actualizara la localizacion en caso de producirse nuevos cambios.*/
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

        /*Se declara y asigna a la clase Location la ultima posicion conocida proporcionada por el proveedor.*/
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        /*
        if (precision == 0.0) {
            loc.setAccuracy((float) constantes.presicionInicialGeolocalizacion);
        }
        */
        capturarPosicion(loc);
        if (precisionCorrecta) {return;}

    }

    /*Metodo que recibe como parametro de entrada una variable de tipo Location, y que permitira
    mostrar los diferentes datos de la ubicacion geografica del dispositivo. En el supuesto de no
    tener habilitada la opcion de ubicacion, se estableceran valores por defecto (dichos valores se almacenaran en un
    array de datos de tipo String).*/
    //private String[] capturarPosicion(Location loc) {
    private void capturarPosicion(Location loc) {

        if(loc != null) {
            precision = loc.getAccuracy();
            if (precision < constantes.presicionGeolocalizacion && precision > 0) {
                latitud = loc.getLatitude();
                longitud = loc.getLongitude();
                precisionCorrecta = true;
            } else {
                precisionCorrecta = false;
            }
        }
        /*
        datos = new String[0];
        if(loc != null) {
            float prec = loc.getAccuracy();
            if (prec < constantes.presicionGeolocalizacion) {
                precisionCorrecta = true;
                //tvPorDefecto.setText("Precision de +-25 mts ");
                datos = new String[]{String.valueOf(loc.getLatitude()),String.valueOf(loc.getLongitude()),String.valueOf(loc.getAccuracy())};
            } else {
                precisionCorrecta = false;
            }
        }
        return datos;
        */
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    /*Se asigna a la clase LocationManager el servicio a nivel de sistema a partir del nombre.*/
                    //locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //tvLatitud.setText(String.valueOf(loc.getLatitude()));
                    //tvLongitud.setText(String.valueOf(loc.getLongitude()));
                    //tvAltura.setText()String.valueOf(loc.getAltitude());
                    //tvPrecision.setText(String.valueOf(loc.getAccuracy()));
                    //String text = "lat: " + String.valueOf(loc.getLatitude()).trim() + ", long: " + String.valueOf(loc.getLongitude()).trim() + ", alt: " + String.valueOf(loc.getAltitude()).trim() + ", precision: " + String.valueOf(loc.getAccuracy()).trim();
                    //Toast.makeText(this, "PASAMOS LA Geolocalizacion Correctamente", Toast.LENGTH_LONG).show();

                    while (precisionCorrecta == false) {
                        rastreoGPS();
                    }
                    String text = "lat: " + latitud + ", long: " + longitud + ", precision: " + precision;
                    Toast.makeText(this, text , Toast.LENGTH_SHORT).show();
                    validaIniTask = new ValidaIniTask(matricula, password);
                    validaIniTask.execute((Void) null);
                    //setResult(RESULT_OK);
                    //finish();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    //tvLatitud.setText("No se han definido los permisos necesarios.");
                    //tvLongitud.setText("");
                    //tvAltura.setText("");
                    //tvPrecision.setText("");
                    Toast.makeText(this, "No se han definido los permisos necesarios. Se cancela la operacion", Toast.LENGTH_SHORT).show();
                    //setResult(constantes.RESULT_FAIL);
                    finish();
                }
                return;
            }
            default: {
                //tvLatitud.setText("No se han definido los permisos necesarios.");
                //tvLongitud.setText("");
                //tvAltura.setText("");
                //tvPrecision.setText("");
                Toast.makeText(this, "No se han definido los permisos necesarios. Se cancela la operacion", Toast.LENGTH_SHORT).show();
                //setResult(constantes.RESULT_FAIL);
                //finish();
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == constantes.RESULT_MAIN_ACTIVITY ) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
            if (resultCode == RESULT_CERRAR_SESION) {
                mPasswordView.setText("");
                mPasswordView.requestFocus();
            }
            if (resultCode == RESULT_HS_ACTIVITY) {
                Intent i = new Intent(getApplicationContext(), HsActivity.class);
                i.putExtra("empresa", empresa);
                i.putExtra("nombreEmpresa", nombreEmpresa);
                i.putExtra("nombre", nombre);
                i.putExtra("cookie", cookie);
                i.putExtra("profesionales", profesionales.toString());
                startActivityForResult(i, RESULT_HS_ACTIVITY);
            }
        }

        if (requestCode == RESULT_HS_ACTIVITY ) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
            if (resultCode == Activity.RESULT_OK) {

                matricula = data.getStringExtra("RESULT_MATRICULA");
                nombre = data.getStringExtra("RESULT_NOMBRE");
                profesional = data.getStringExtra("RESULT_PROFESIONAL");
                nombreEmpresa = data.getStringExtra("RESULT_NOMBRE_EMPRESA");
                password = "";

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("activity", constantes.HS_ACTIVITY);
                i.putExtra("empresa", empresa);
                i.putExtra("nombreEmpresa", nombreEmpresa);
                i.putExtra("nombre", nombre);
                i.putExtra("matricula", matricula);
                i.putExtra("password", password);
                i.putExtra("profesional", profesional);
                i.putExtra("cookie", cookie);
                startActivityForResult(i,constantes.RESULT_MAIN_ACTIVITY);
            }
        }
        /*
        if (requestCode == constantes.RESULT_GEOLOCALIZACION ) {
            if (resultCode == constantes.RESULT_FAIL) {
                finish();
            }
            if (resultCode == RESULT_OK) {


                datos = data.getStringArrayExtra("RESULT_DATOS");
                String msj = "lat: " + datos[0] + ", long: " + datos[1] + ", precision: " + datos[2];

                Toast.makeText(this, msj, Toast.LENGTH_LONG).show();
                //validaIniTask = new ValidaIniTask(matricula, password);
                //validaIniTask.execute((Void) null);
            }
        }
        */

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
                conn.setRequestProperty("Content-Type: application/json", "charset=utf-8");
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
                        //geolocalizacionTask = new GeolocalizacionTask();
                        //geolocalizacionTask.execute((Void) null);

                        //fetchCordinates = new FetchCordinates();
                        //fetchCordinates.execute();

                        if (mMatricula.trim().equals(constantes.cuitHS) && mPassword.trim().toUpperCase().equals(constantes.passHS)){
                            hsLoginTask = new HsLoginTask();
                            hsLoginTask.execute((Void) null);
                        } else {
                            userLoginTask = new UserLoginTask(mMatricula, mPassword);
                            userLoginTask.execute((Void) null);
                        }
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
                conn.setRequestProperty("Content-Type: application/json", "charset=utf-8");
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
            userLoginTask = null;
            showProgress(false);

            JSONObject jsonObject = null;

            int salida = 0;
            int flag = 0;
            nombre = "";
            String fecha = "";
            nombreEmpresa = "";

            try {
                jsonObject = jsonResp.getJSONObject("Response");

                flag = jsonObject.getInt("flag");
                nombre = jsonObject.getString("nombre");
                nombreEmpresa = jsonObject.getString("comentario");
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
                        i.putExtra("activity", constantes.LOGIN_ACTIVITY);
                        i.putExtra("empresa", empresa);
                        i.putExtra("nombreEmpresa", nombreEmpresa);
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
            userLoginTask = null;
            showProgress(false);
        }
    }

    public class HsLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mMatricula;
        private final String mPassword;
        private JSONObject jsonResp = null;

        public HsLoginTask() {
            mMatricula = constantes.cuitHS;
            mPassword = constantes.passHS;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + constantes.metodoGetProfesionales);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type: application/json", "charset=utf-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("Cookie",cookie);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

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
            hsLoginTask = null;
            showProgress(false);

            JSONObject jsonObject = null;
            profesionales = null;

            int salida = 0;
            int registros = 0;
            String nombre = "";
            String fecha = "";
            String nombreEmpresa = "";

            try {
                jsonObject = jsonResp.getJSONObject("Response");

                registros = jsonObject.getInt("registros");
                nombreEmpresa = jsonObject.getString("empresa");
                profesionales = jsonObject.getJSONArray("Profesionales");

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
                        Intent i = new Intent(getApplicationContext(), HsActivity.class);
                        i.putExtra("empresa", empresa);
                        i.putExtra("nombreEmpresa", nombreEmpresa);
                        i.putExtra("nombre", nombre);
                        i.putExtra("cookie", cookie);
                        i.putExtra("profesionales", profesionales.toString());
                        startActivityForResult(i, RESULT_HS_ACTIVITY);

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
            hsLoginTask = null;
            showProgress(false);
        }
    }





/*
    public class GeolocalizacionTask extends AsyncTask<Void, Void, Boolean> {

        private final float latitud;
        private final float longitud;
        private final float precision;

        public GeolocalizacionTask() {
            latitud = 0;
            longitud = 0;
            precision = 0;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            capturarGeoLocalizacion();
            return precisionCorrecta;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {

                String mensaje = "lat: " + latitud + ", long: " + longitud + ", precision: " + precision;
                //String mensaje = "lat: " + datos[0] + ", long: " + datos[1] + ", precision: " + datos[2];
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            geolocalizacionTask = null;
            showProgress(false);
        }
    }

    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;

        public double lati = 0.0;
        public double longi = 0.0;
        public double precision = constantes.presicionInicialGeolocalizacion;

        public LocationManager mLocationManager;
        public VeggsterLocationListener mVeggsterLocationListener;

        @Override
        protected void onPreExecute() {
            mVeggsterLocationListener = new VeggsterLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0,
                    mVeggsterLocationListener);


            progDailog = new ProgressDialog(LoginActivity.this);
            progDailog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    FetchCordinates.this.cancel(true);
                }
            });
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();


        }

        @Override
        protected void onCancelled(){
            System.out.println("Cancelled by user!");
            progDailog.dismiss();
            mLocationManager.removeUpdates(mVeggsterLocationListener);
        }

        @Override
        protected void onPostExecute(String result) {
            progDailog.dismiss();

            Toast.makeText(LoginActivity.this,
                    "LATITUDE :" + lati + " LONGITUDE :" + longi + " PRECISION :" + precision,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            //while (this.lati == 0.0) {
            while (this.precision > constantes.presicionGeolocalizacion) {

            }
            return null;
        }

        public class VeggsterLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {

                    // LocatorService.myLatitude=location.getLatitude();

                    // LocatorService.myLongitude=location.getLongitude();

                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    precision = location.getAccuracy();

                } catch (Exception e) {
                    // progDailog.dismiss();
                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
                    // , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");

            }

        }

    }
*/
}