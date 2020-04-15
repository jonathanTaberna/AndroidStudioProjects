package jt.cotitagliero;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import jt.cotitagliero.menu_lateral.ClienteFragment;
import jt.cotitagliero.constantes.*;

public class ClienteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar toolbar;

    private String id;
    private String nombre;
    private ClienteFragment clienteFragment;
    private Bundle argumentosClientes = new Bundle();

    ConstraintLayout constraintLayout;
    NavigationView navigationView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        id = extras.getString("id");
        nombre = extras.getString("nombre");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.ivNavigator);

        this.setTitle("Coti Tagliero Nutricion titulo");
        imgvw.setImageResource(R.drawable.logo_coti);
        /*
        if (!activity.equals(constantes.HS_ACTIVITY)){
            Menu menu = navigationView.getMenu();
            for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
                MenuItem menuItem = menu.getItem(menuItemIndex);
                if (menuItem.getTitle().equals("Acciones")) {
                    SubMenu subMenu =  menuItem.getSubMenu();
                    for (int i = 0; i < subMenu.size(); i++) {
                        MenuItem menuItem1 = subMenu.getItem(i);
                        if (menuItem1.getItemId() == R.id.nav_hs_menu_item) {
                            menuItem1.setVisible(false);
                        }
                    }
                }
            }
        }
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        llamarNewInstanceClientes();
        navigationView.getMenu().getItem(0).setChecked(true); //marca el primer item de la lista del menu lateral
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
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

        if (id == R.id.nav_cerrar_sesion) {
            setResult(constantes.RESULT_CERRAR_SESION);
            finish();

        } else if (id == R.id.nav_salir) {
            setResult(RESULT_CANCELED);
            clienteFragment.clientesVector.eliminaListaClientes();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void llamarNewInstanceClientes(){

        argumentosClientes.putString("id", id);
        argumentosClientes.putString("nombre", nombre);
        clienteFragment = new ClienteFragment();
        clienteFragment.setArguments(argumentosClientes);
        fragmentManager.beginTransaction().replace(R.id.contenedor, clienteFragment).commit();

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
