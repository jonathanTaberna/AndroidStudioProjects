package hsneoclinica.neoclinica;

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
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;

import hsneoclinica.neoclinica.constantes.constantes;
import hsneoclinica.neoclinica.menu_lateral.DiasNoTrabajoFragment;
import hsneoclinica.neoclinica.menu_lateral.InternadosFragment;
import hsneoclinica.neoclinica.menu_lateral.TurnoFragment;

import static hsneoclinica.neoclinica.constantes.constantes.RESULT_CERRAR_SESION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private String fragmentActual = "";
    private Toolbar toolbar;
    private Boolean enableView = false;

    private String activity;
    private String empresa;
    private String nombreEmpresa;
    private String nombre;
    private String cookie;
    private String matricula;
    private String profesional;
    private String password;
    //private ArrayList<Turno> elementos = new ArrayList<Turno>();

    private TurnoFragment turnoFragment;
    private InternadosFragment internadosFragment;
    private DiasNoTrabajoFragment diasNoTrabajoFragment;
    private Bundle argumentosAgenda = new Bundle();
    private Bundle argumentosInternado = new Bundle();
    private Bundle argumentosDiasNoTrabajo = new Bundle();

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

        activity = extras.getString("activity");
        empresa = extras.getString("empresa");
        nombreEmpresa = extras.getString("nombreEmpresa");
        nombre = extras.getString("nombre");
        matricula = extras.getString("matricula");
        password = extras.getString("password");
        profesional = extras.getString("profesional");
        cookie = extras.getString("cookie");

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.ivNavigator);
        this.setTitle(nombreEmpresa);
        switch (empresa){
            case "SanLucas":
                imgvw.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "SanatorioPrivado":
                imgvw.setImageResource(R.drawable.sanatorio_privado_logo);
                break;
            case "Neoclinica":
                imgvw.setImageResource(R.drawable.neoclinica_logo);
                break;
            case "Odontograssi":
                imgvw.setImageResource(R.drawable.odontograssi_logo);
                break;
            case "ResonanciaR4":
                imgvw.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "Urologico":
                imgvw.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "ClinicaPrivGralDeheza":
                imgvw.setImageResource(R.drawable.clinica_gral_deheza_logo);
                break;
            case "HospitalComGralDeheza":
                imgvw.setImageResource(R.drawable.san_lucas_logo);
                break;
            case "HS":
                break;
            default:
                 break;
        }
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentActual = "agenda";
        llamarNewInstanceAgenda(fragmentActual);
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

        if (id == R.id.nav_agenda) {
            if (fragmentActual != "agenda") {
                fragmentActual = "agenda";
                llamarNewInstanceAgenda(fragmentActual);
            }
            // Handle the camera action
        } else if (id == R.id.nav_internados) {
            //toolbar.getMenu().findItem(R.id.main2_action_refresh).setVisible(false);
            if (fragmentActual != "internados") {
                fragmentActual = "internados";
                llamarNewInstanceInternado(fragmentActual);
            }

        } else if (id == R.id.nav_dias_no_atiendo) {
            //toolbar.getMenu().findItem(R.id.main2_action_refresh).setVisible(false);
            if (fragmentActual != "diasNoAtiendo") {
                fragmentActual = "diasNoAtiendo";
                llamarNewInstanceDiasNoAtiendo(fragmentActual);
            }

        } else if (id == R.id.nav_hs_menu_item) {
            setResult(constantes.RESULT_HS_ACTIVITY);
            turnoFragment.turnosVector.eliminaListaProductos();
            finish();

        } else if (id == R.id.nav_cerrar_sesion) {
            setResult(RESULT_CERRAR_SESION);
            finish();

        } else if (id == R.id.nav_salir) {
            setResult(RESULT_CANCELED);
            turnoFragment.turnosVector.eliminaListaProductos();
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

    private void llamarNewInstanceInternado(String fragmentActual){

        argumentosInternado.putString("fragmentVisible", fragmentActual);
        argumentosInternado.putString("nombre", nombre);
        argumentosInternado.putString("matricula", matricula);
        argumentosInternado.putString("profesional", profesional);
        argumentosInternado.putString("password", password);
        argumentosInternado.putString("cookie", cookie);

        internadosFragment = new InternadosFragment();
        internadosFragment.setArguments(argumentosInternado);
        fragmentManager.beginTransaction().replace(R.id.contenedor, internadosFragment).commit();

    }
    
    private void llamarNewInstanceDiasNoAtiendo(String fragmentActual){

        argumentosDiasNoTrabajo.putString("nombre", nombre);
        argumentosDiasNoTrabajo.putString("profesional", profesional);
        argumentosDiasNoTrabajo.putString("password", password);
        argumentosDiasNoTrabajo.putString("cookie", cookie);

        diasNoTrabajoFragment = new DiasNoTrabajoFragment();
        diasNoTrabajoFragment.setArguments(argumentosDiasNoTrabajo);
        fragmentManager.beginTransaction().replace(R.id.contenedor, diasNoTrabajoFragment).commit();

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
