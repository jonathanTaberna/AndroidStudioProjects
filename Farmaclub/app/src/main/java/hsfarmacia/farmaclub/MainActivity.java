package hsfarmacia.farmaclub;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import hsfarmacia.farmaclub.constantes.constantes;
import hsfarmacia.farmaclub.menu_lateral.CanjeFragment;
import hsfarmacia.farmaclub.menu_lateral.MisDatosFragment;
import hsfarmacia.farmaclub.menu_lateral.NovedadesFragment;
import hsfarmacia.farmaclub.provisorios.Check;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    ConfiguracionesDialogo.FinalizoConfiguracionesDialogo,
                    PreferenciasDialogo.FinalizoPreferenciasDialogo {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private String fragmentActual = "";
    private Toolbar toolbar;
    private Boolean enableView = false;
    private TextView tvCantidadNotificaciones;

    private String tarjeta;
    private int puntos;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;
    private String localidad;
    private int codpos;
    private String fecnac;

    private String filtrarPuntos;
    private int filtrarPor;
    private int ordenarPor;
    private int ordenar;

    private CanjeFragment canjeFragment;
    private Bundle argumentosCanje = new Bundle();

    private MisDatosFragment misDatosFragment;
    private UpdateDataPersonTask updateDataPersonTask = null;
    private GetPreferenciasTask getPreferenciasTask = null;

    private NovedadesFragment novedadesFragment;

    private ArrayList<Check> elementos = new ArrayList<Check>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvCantidadNotificaciones = (TextView) navigationView.getMenu().findItem(R.id.nav_novedades).getActionView().findViewById(R.id.tvCantidadNotificaciones);
        tvCantidadNotificaciones.setText("99+");

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        tarjeta = extras.getString("tarjeta");
        puntos = extras.getInt("puntos");
        nombre = extras.getString("nombre");
        correo = extras.getString("correo");
        telefono = extras.getString("telefono");
        direccion = extras.getString("direccion");
        localidad = extras.getString("localidad");
        codpos = extras.getInt("codpos");
        fecnac = extras.getString("fecnac");

        filtrarPuntos = "todos";
        filtrarPor = 1;
        ordenarPor = 1;
        ordenar = 1;


        fragmentActual = "canje";
        llamarNewInstanceCanje(fragmentActual);
        navigationView.getMenu().getItem(0).setChecked(true); //marca el primer item de la lista del menu lateral











        elementos.add(new Check(1, "CheckBox 1", false));
        elementos.add(new Check(2, "CheckBox 2", false));
        elementos.add(new Check(3, "CheckBox 3", false));
        elementos.add(new Check(4, "CheckBox 4", false));
        elementos.add(new Check(5, "CheckBox 5", false));
        elementos.add(new Check(6, "CheckBox 6", false));
        elementos.add(new Check(7, "CheckBox 7", false));
        elementos.add(new Check(8, "CheckBox 8", false));
        elementos.add(new Check(9, "CheckBox 9", false));
        elementos.add(new Check(10, "CheckBox 10", false));
        elementos.add(new Check(11, "CheckBox 11", false));
        elementos.add(new Check(12, "CheckBox 12", false));
        elementos.add(new Check(13, "CheckBox 13", false));
        elementos.add(new Check(14, "CheckBox 14", false));
        elementos.add(new Check(15, "CheckBox 15", false));
        elementos.add(new Check(16, "CheckBox 16", false));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragmentActual == "mis_datos" && enableView) {
                toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(true);
                llamarNewInstanceMisDatos(false);
                //enableView = false;
                //misDatosFragment = MisDatosFragment.newInstance(enableView, nombre, correo, telefono, direccion, localidad, codpos);
                //fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();

            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            //super.onBackPressed(); //close the app
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main2_action_settings) {
            new ConfiguracionesDialogo(MainActivity.this, MainActivity.this, filtrarPor, ordenarPor,ordenar);
            return true;
        }
        if (id == R.id.main2_action_preferences) {


            getPreferenciasTask = new GetPreferenciasTask();
            getPreferenciasTask.execute((Void) null);

            //new PreferenciasDialogo(MainActivity.this, MainActivity.this, elementos);
            return true;
        }
        if (id == R.id.main2_action_edit_data) {

            toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(false);


            //argumentosMisDatos.putBoolean("vistaActiva", true);
            //misDatosFragment.getArguments().clear();
            //misDatosFragment = new MisDatosFragment();
            //misDatosFragment.setArguments(argumentosMisDatos);
            //fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();
            llamarNewInstanceMisDatos(true);
            //enableView = true;
            //misDatosFragment = MisDatosFragment.newInstance(enableView, nombre, correo, telefono, direccion, localidad, codpos);
            //fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_canje) {
            if (fragmentActual != "canje") {
                fragmentActual = "canje";
                llamarNewInstanceCanje(fragmentActual);
                //fragmentManager.beginTransaction().replace(R.id.contenedor, canjeFragment).commit();
                toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(true);
                toolbar.getMenu().findItem(R.id.main2_action_preferences).setVisible(false);
                toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(false);

                this.filtrarPuntos = "todos";
                this.filtrarPor = 1;
                this.ordenarPor = 1;
                this.ordenar = 1;
            }
            // Handle the camera action
        } else if (id == R.id.nav_promociones) {
            fragmentActual = "promociones";
            llamarNewInstanceCanje(fragmentActual);

            /*
            argumentosCanje.putString("fragmentVisible", fragmentActual);
            canjeFragment = new CanjeFragment();
            canjeFragment.setArguments(argumentosCanje);

            //fragmentManager.beginTransaction().replace(R.id.contenedor, new PromocionesFragment()).commit();
            fragmentManager.beginTransaction().replace(R.id.contenedor, canjeFragment).commit();
            */
            toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(false);
            toolbar.getMenu().findItem(R.id.main2_action_preferences).setVisible(true);
            toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(false);

        } else if (id == R.id.nav_mis_datos) {
            fragmentActual = "mis_datos";

            //argumentosMisDatos.putBoolean("vistaActiva", false);
            //misDatosFragment = new MisDatosFragment();
            //misDatosFragment.setArguments(argumentosMisDatos);
            llamarNewInstanceMisDatos(false);
            //enableView = false;
            //misDatosFragment = MisDatosFragment.newInstance(enableView, nombre, correo, telefono, direccion, localidad, codpos);
            //fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();

            toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(false);
            toolbar.getMenu().findItem(R.id.main2_action_preferences).setVisible(false);
            toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(true);

        } else if (id == R.id.nav_novedades) {
            fragmentActual = "novedades";
            llamarNewInstanceNovedades(tarjeta);
            //fragmentManager.beginTransaction().replace(R.id.contenedor, new NovedadesFragment()).commit();
            toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(false);
            toolbar.getMenu().findItem(R.id.main2_action_preferences).setVisible(false);
            toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(false);

        } else if (id == R.id.nav_cerrar_sesion) {
            setResult(RESULT_FIRST_USER);
            finish();

        } else if (id == R.id.nav_salir) {
            setResult(RESULT_CANCELED);
            canjeFragment.productosVector.eliminaListaProductos();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void ResultadoConfiguracionesDialogo(String filtrarPuntos, String orden, String orderBy) {
        this.filtrarPuntos = filtrarPuntos;

        if (filtrarPuntos == "todos") {
            this.filtrarPor = 1;
        } else {
            this.filtrarPor = 2;
        }
        if (orden == "nombre") {
            this.ordenarPor = 1;
        } else {
            this.ordenarPor = 2;
        }
        if (orderBy == "asc") {
            this.ordenar = 1;
        } else {
            this.ordenar = 2;
        }
        canjeFragment.actualizarVistaOrdenada(filtrarPuntos, orden, orderBy);
    }

    public void ResultadoMisDatos(String accion, String nombre, String correo, String telefono, String direccion, String localidad, int codpos, String fecnac) {
        toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(true);

        if (accion == "ACEPTAR") {
            Toast.makeText(MainActivity.this,"APRETO ACEPTAR", Toast.LENGTH_SHORT).show();

            updateDataPersonTask = new UpdateDataPersonTask(nombre,correo,telefono, direccion, localidad, codpos, fecnac);
            updateDataPersonTask.execute((Void) null);

        }
        if (accion == "CANCELAR"){
            Toast.makeText(MainActivity.this,"APRETO CANCELAR", Toast.LENGTH_SHORT).show();
            llamarNewInstanceMisDatos(false);
            //enableView = false;
            //misDatosFragment = MisDatosFragment.newInstance(enableView, nombre, correo, telefono, direccion, localidad, codpos);
            //fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();
        }
    }

    @Override
    public void ResultadoPreferenciasDialogo(ArrayList<Check> elementos) {

    }

    private void llamarNewInstanceNovedades(String tarjeta){
        novedadesFragment = NovedadesFragment.newInstance(tarjeta);
        fragmentManager.beginTransaction().replace(R.id.contenedor, novedadesFragment).commit();
    }

    private void llamarNewInstanceMisDatos(Boolean enable){

        enableView = enable;
        misDatosFragment = MisDatosFragment.newInstance(enableView, nombre, correo, telefono, direccion, localidad, codpos, fecnac);
        fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();

    }

    private void llamarNewInstanceCanje(String fragmentActual){

        argumentosCanje.putString("fragmentVisible", fragmentActual);
        argumentosCanje.putString("tarjeta", tarjeta);
        argumentosCanje.putInt("puntos", puntos);
        argumentosCanje.putString("nombre", nombre);

        canjeFragment = new CanjeFragment();
        canjeFragment.setArguments(argumentosCanje);
        fragmentManager.beginTransaction().replace(R.id.contenedor, canjeFragment).commit();

    }


    public class UpdateDataPersonTask extends AsyncTask<Void, Void, Boolean> {

        private JSONObject respuestaResetPass = new JSONObject();
        private int respuestaStatus = 0;
        private String nombreUpdate;
        private String correoUpdate;
        private String telefonoUpdate;
        private String direccionUpdate;
        private String localidadUpdate;
        private int codposUpdate;
        private String fecnacUpdate;

        public UpdateDataPersonTask (String nombreUpdate, String correoUpdate, String telefonoUpdate, String direccionUpdate, String localidadUpdate, int codposUpdate, String fecnacUpdate) {
            this.nombreUpdate = nombreUpdate;
            this.correoUpdate = correoUpdate;
            this.telefonoUpdate = telefonoUpdate;
            this.direccionUpdate = direccionUpdate;
            this.localidadUpdate = localidadUpdate;
            this.codposUpdate = codposUpdate;
            this.fecnacUpdate = fecnacUpdate;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String JsonResponse = null;
            try {
                URL url = new URL( constantes.pathConnection + "actualizaDatos");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setConnectTimeout(10000); //10 segundos
                conn.connect();

                obj.put("tarjeta", tarjeta);
                obj.put("nombre", nombreUpdate);
                obj.put("correo", correoUpdate);
                obj.put("telefono", telefonoUpdate);
                obj.put("direccion", direccionUpdate);
                obj.put("localidad", localidadUpdate);
                obj.put("codpos", codposUpdate);
                obj.put("fecnac", fecnacUpdate);


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
            updateDataPersonTask = null;

            //variables de respuesta
            int salida = 9;
            String msj = "";

            try {
                salida = respuestaResetPass.getInt("salida");
                msj = respuestaResetPass.getString("msj");
            } catch (Exception e){
                salida = 9;
                return;
            }

            switch (salida) {
                case 1:
                    if (success) {
                        nombre = nombreUpdate;
                        correo = correoUpdate;
                        telefono = telefonoUpdate;
                        direccion = direccionUpdate;
                        localidad = localidadUpdate;
                        codpos = codposUpdate;
                        fecnac = fecnacUpdate;

                        llamarNewInstanceMisDatos(false);
                        //enableView = false;
                        //misDatosFragment = MisDatosFragment.newInstance(enableView, nombre, correo, telefono, direccion, localidad, codpos);
                        //fragmentManager.beginTransaction().replace(R.id.contenedor, misDatosFragment).commit();
                    }
                    break;
                case 2:
                case 3:
                case 4:
                    Toast.makeText(MainActivity.this, msj, Toast.LENGTH_LONG).show();
                    break;
                case 9:
                    if (respuestaStatus == 99) {
                        Toast.makeText(MainActivity.this, getString(R.string.servidor_timeout), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.toastErrorReestablecerUsuario), Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    Toast.makeText(MainActivity.this, getString(R.string.toastErrorReestablecerUsuario), Toast.LENGTH_LONG).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            updateDataPersonTask = null;
        }
    }


    public class GetPreferenciasTask extends AsyncTask<Void, Void, Boolean> {
        private int status = 0;
        private int cantidadPreferencias = 0;
        JSONObject jsonResp = null;

        public GetPreferenciasTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject obj = new JSONObject();
            HttpURLConnection conn = null;

            BufferedReader reader = null;
            String JsonResponse = null;
            try {

                URL url = new URL(constantes.pathConnectionProductos + "getCatUsu");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
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

                    }
                    JsonResponse = buffer.toString();
                    jsonResp = new JSONObject(JsonResponse);

                }

            } catch (ConnectException ce) {
                if (ce.getMessage().contains("ETIMEDOUT")) {
                    status = 99;
                } else {
                    status = 98;
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
            getPreferenciasTask = null;
            //showProgress(false);

            int salida = 9;
            String msj = "";
            JSONArray jsonArray = null;

            try {
                salida = jsonResp.getInt("salida");
                msj = jsonResp.getString("msj");
                cantidadPreferencias = jsonResp.getInt("cantidad");
                jsonArray = jsonResp.getJSONArray("preferencias");
                System.out.println(jsonArray.toString());
            } catch (Exception e) {
                salida = 8;
            }

            switch (salida) {
                case 1:
                case 8:
                    if (salida == 8) {
                        if (status == 98) {
                            Toast.makeText(MainActivity.this, getString(R.string.servidor_timeout), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (status == 404  || status == 405) {
                            Toast.makeText(MainActivity.this, getString(R.string.servidor_error), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (success) {

                        Toast.makeText(MainActivity.this, "success",Toast.LENGTH_SHORT);
                        Log.i("onPost","success");

                        new PreferenciasDialogo(MainActivity.this, MainActivity.this, elementos);

                        //updateUserTask = new UsuarioActivity.UpdateUserTask(usuario,edtPassword.getText().toString(),tarjeta);
                        //updateUserTask.execute((Void) null);
                    } else {
                        Toast.makeText(MainActivity.this, "no success",Toast.LENGTH_SHORT);
                        Log.i("onPost","no success");
                        //edtUsuario.setError(getString(R.string.error_json));
                        //edtUsuario.requestFocus();
                    }
                    break;

                case 9:
                    if (status == 99 || status == 404  || status == 405) {
                        Toast.makeText(MainActivity.this, "status 99",Toast.LENGTH_SHORT);
                        Log.i("onPost","status 99");
                        //edtUsuario.setError(getString(R.string.servidor_timeout));
                        //edtUsuario.requestFocus();
                    } else {
                        Toast.makeText(MainActivity.this, "status 9",Toast.LENGTH_SHORT);
                        Log.i("onPost","status 9");
                        //edtUsuario.setError(getString(R.string.usuario_existente));
                        //edtUsuario.requestFocus();
                    }
                    break;
                default:
                    Toast.makeText(MainActivity.this, "default",Toast.LENGTH_SHORT);
                    Log.i("onPost","default");
                    //edtPassword.setError(getString(R.string.error_incorrect_password));
                    //edtPassword.requestFocus();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            //showProgress(false);
            getPreferenciasTask = null;
        }
    }

}
