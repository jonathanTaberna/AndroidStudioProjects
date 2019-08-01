package hsneoclinica.neoclinica;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.RatingCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import hsneoclinica.neoclinica.constantes.constantes;
import hsneoclinica.neoclinica.menu_lateral.TurnoFragment;
import hsneoclinica.neoclinica.provisorios.Check;
import hsneoclinica.neoclinica.provisorios.Turno;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private String fragmentActual = "";
    private Toolbar toolbar;
    private Boolean enableView = false;

    private String nombre;
    private String cookie;
    private String matricula;
    private String profesional;
    private String password;
    //private ArrayList<Turno> elementos = new ArrayList<Turno>();

    private TurnoFragment turnoFragment;
    private Bundle argumentosAgenda = new Bundle();

    ConstraintLayout constraintLayout;
    NavigationView navigationView;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0); //inflateHeaderView(R.layout.nav_header_main2);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.ivNavigator);
        imgvw.setImageResource(R.drawable.neoclinica_logo);
        this.setTitle("Este Titulo se puede modificar");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        nombre = extras.getString("nombre");
        matricula = extras.getString("matricula");
        password = extras.getString("password");
        profesional = extras.getString("profesional");
        cookie = extras.getString("cookie");
        //elementos = (ArrayList<Turno>) extras.getSerializable("elementos");
        fragmentActual = "agenda";
        //toolbar.getMenu().findItem(R.id.main2_action_refresh).setVisible(false);
        llamarNewInstanceAgenda(fragmentActual);
        navigationView.getMenu().getItem(0).setChecked(true); //marca el primer item de la lista del menu lateral
        //navigationView.getMenu().getItem(0).setIconTintList(ColorStateList.valueOf(R.color.titleColor)); //marca el primer item de la lista del menu lateral
        //setTitleIconDrawer(navigationView);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
            /*
            if (fragmentActual == "mis_datos" && enableView) {
                toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(true);
                llamarNewInstanceMisDatos(false);

            } else {
            }*/
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
        if (id == R.id.main2_action_refresh) {
            //fragmentActual = "agenda";
            //llamarNewInstanceAgenda(fragmentActual);
            //new ConfiguracionesDialogo(MainActivity.this, MainActivity.this, 0, 0,0);
            return true;
        }
        /*
        if (id == R.id.main2_action_preferences) {

            //new PreferenciasDialogo(MainActivity.this, MainActivity.this, elementos);
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            /*
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(R.color.titleColor), 0, spanString.length(), 0); // fix the color to white
            item.setTitle(spanString);
            */

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_agenda) {
            if (fragmentActual != "agenda") {
                fragmentActual = "agenda";
                llamarNewInstanceAgenda(fragmentActual);
                //fragmentManager.beginTransaction().replace(R.id.contenedor, turnoFragment).commit();
                //toolbar.getMenu().findItem(R.id.main2_action_refresh).setVisible(false);
                //toolbar.getMenu().findItem(R.id.main2_action_preferences).setVisible(false);
                //toolbar.getMenu().findItem(R.id.main2_action_edit_data).setVisible(false);

            }
            // Handle the camera action
        } else if (id == R.id.nav_internados) {
            //toolbar.getMenu().findItem(R.id.main2_action_refresh).setVisible(false);
            if (fragmentActual != "internados") {
                fragmentActual = "internados";
            }

        } else if (id == R.id.nav_dias_no_atiendo) {
            //toolbar.getMenu().findItem(R.id.main2_action_refresh).setVisible(false);
            if (fragmentActual != "diasNoAtiendo") {
                fragmentActual = "diasNoAtiendo";
            }

        } else if (id == R.id.nav_cerrar_sesion) {
            setResult(RESULT_FIRST_USER);
            finish();

        } else if (id == R.id.nav_salir) {
            setResult(RESULT_CANCELED);
            turnoFragment.productosVector.eliminaListaProductos();
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void llamarNewInstanceAgenda(String fragmentActual){

        argumentosAgenda.putString("fragmentVisible", fragmentActual);
        argumentosAgenda.putString("nombre", nombre);
        argumentosAgenda.putString("matricula", matricula);
        argumentosAgenda.putString("profesional", profesional);
        argumentosAgenda.putString("password", password);
        argumentosAgenda.putString("cookie", cookie);
        //argumentosAgenda.putSerializable("elementos",elementos);

        turnoFragment = new TurnoFragment();
        turnoFragment.setArguments(argumentosAgenda);
        fragmentManager.beginTransaction().replace(R.id.contenedor, turnoFragment).commit();

    }

    private void setTitleIconDrawer(NavigationView navigationView){
        SpannableString spanString;
        MenuItem item;
        for (int i=0; i < 3; i++) {
            item = navigationView.getMenu().getItem(i);
            spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); // fix the color to white
            item.setTitle(spanString);
        }
    }
}
