package hsfarmacia.farmaclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hsfarmacia.farmaclub.menu_lateral.CanjeFragment;
import hsfarmacia.farmaclub.menu_lateral.MisDatosFragment;
import hsfarmacia.farmaclub.menu_lateral.NovedadesFragment;
import hsfarmacia.farmaclub.menu_lateral.PromocionesFragment;

import static hsfarmacia.farmaclub.constantes.constantes.CANTIDAD_PRODUCTOS_LISTA;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConfiguracionesDialogo.FinalizoConfiguracionesDialogo{

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private String fragmentActual = "";
    private Toolbar toolbar;


    private String tarjeta;
    private int puntos;
    private String nombre;

    private String filtrarPuntos;
    private int filtrarPor;
    private int ordenarPor;
    private int ordenar;

    private CanjeFragment canjeFragment;
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



        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        tarjeta = extras.getString("tarjeta");
        puntos = extras.getInt("puntos");
        nombre = extras.getString("nombre");

        filtrarPuntos = "todos";
        filtrarPor = 1;
        ordenarPor = 1;
        ordenar = 1;

        fragmentActual = "canje";
        canjeFragment = new CanjeFragment(tarjeta,puntos,nombre);
        //fragmentManager.beginTransaction().replace(R.id.contenedor, new CanjeFragment(tarjeta,puntos,nombre)).commit();
        fragmentManager.beginTransaction().replace(R.id.contenedor, canjeFragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            new ConfiguracionesDialogo(MainActivity2.this, MainActivity2.this, filtrarPor, ordenarPor,ordenar);
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
                fragmentManager.beginTransaction().replace(R.id.contenedor, new CanjeFragment(tarjeta,puntos,nombre)).commit();
                toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(true);

                this.filtrarPuntos = "todos";
                this.filtrarPor = 1;
                this.ordenarPor = 1;
                this.ordenar = 1;
            }
            // Handle the camera action
        } else if (id == R.id.nav_promociones) {
            fragmentActual = "promociones";
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PromocionesFragment()).commit();
            toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(false);

        } else if (id == R.id.nav_mis_datos) {
            fragmentActual = "mis_datos";
            fragmentManager.beginTransaction().replace(R.id.contenedor, new MisDatosFragment()).commit();
            toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(false);

        } else if (id == R.id.nav_novedades) {
            fragmentActual = "novedades";
            fragmentManager.beginTransaction().replace(R.id.contenedor, new NovedadesFragment()).commit();
            toolbar.getMenu().findItem(R.id.main2_action_settings).setVisible(false);

        } else if (id == R.id.nav_cerrar_sesion) {
            setResult(RESULT_FIRST_USER);
            finish();

        } else if (id == R.id.nav_salir) {
            setResult(RESULT_CANCELED);
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



}
