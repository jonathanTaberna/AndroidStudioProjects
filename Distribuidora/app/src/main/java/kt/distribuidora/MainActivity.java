package kt.distribuidora;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import kt.distribuidora.constantes.constantes;
import kt.distribuidora.fragments_datos_cargados.ArticulosCargadosFragment;
import kt.distribuidora.fragments_secundarios.ConfiguracionesDialogo;
import kt.distribuidora.menu_lateral.CambiarPassFragment;
import kt.distribuidora.fragments_datos_cargados.ClientesCargadosFragment;
import kt.distribuidora.fragments_datos_cargados.ClientesNuevosCargadosFragment;
import kt.distribuidora.menu_lateral.ImportarDatosFragment;
import kt.distribuidora.menu_lateral.MenuFragment;
import kt.distribuidora.menu_lateral.NuevoClienteFragment;
import kt.distribuidora.fragments_secundarios.NuevoPedidoFragment;
import kt.distribuidora.fragments_datos_cargados.PedidosCargadosFragment;
import kt.distribuidora.sql.AdminSQLiteOpenHelper;
import kt.distribuidora.elementos.ClienteNuevo;
import kt.distribuidora.elementos.Pedido;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ConfiguracionesDialogo.FinalizoConfiguracionesDialogo {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar toolbar;

    private String fragmentActual = "";
    private Boolean flagNuevoPedido;
    private Boolean flagClientesNuevosCargados;
    private Boolean flagClientesCargados;
    private Boolean flagArticulosCargados;
    private Boolean flagPedidosCargados;
    private Boolean flagPedidosGuardados;

    private long codigoCliente;
    private String nombreCliente;

    private int ordenarPor;
    private int ordenar;

    private MenuFragment menuFragment;
    private ImportarDatosFragment importarDatosFragment;
    private NuevoClienteFragment nuevoClienteFragment;
    private CambiarPassFragment cambiarPassFragment;
    private ClientesCargadosFragment clientesCargadosFragment;
    private ArticulosCargadosFragment articulosCargadosFragment;
    private ClientesNuevosCargadosFragment clientesNuevosCargadosFragment;
    private NuevoPedidoFragment nuevoPedidoFragment;
    private PedidosCargadosFragment pedidosCargadosFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlanco));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNegro)));
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNegro)));

        pedirPermisos();


        this.ordenarPor = 1;
        this.ordenar = 1;

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        admin.crearTablas(db);
        db.close();
        admin.close();

        fragmentActual = "menuPrincipal";
        setearFlags(false, false, false, false, false, false);

        llamarNewInstanceMenu(fragmentActual);
        navigationView.getMenu().getItem(0).setChecked(true); //marca el primer item de la lista del menu lateral
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //if (fragmentActual == "importarDatos" && (flagClientesCargados == true || flagArticulosCargados == true || flagVendedoresCargados == true)) {
            if (fragmentActual == "importarDatos" && (flagClientesCargados == true || flagArticulosCargados == true)) {
                toolbar.getMenu().findItem(R.id.main2_action_listar_articulos_cargados).setVisible(true);
                toolbar.getMenu().findItem(R.id.main2_action_listar_clientes_cargados).setVisible(true);
                llamarNewInstanceImportarDatos(fragmentActual);
            } else if (fragmentActual == "nuevoCliente" && flagClientesNuevosCargados == true){
                toolbar.getMenu().findItem(R.id.main2_action_listar_clientes_nuevos).setVisible(true);
                toolbar.getMenu().findItem(R.id.main2_action_borrar_clientes_nuevos_cargados).setVisible(true);
                llamarNewInstanceNuevoCliente(fragmentActual,false, null);
            } else if (fragmentActual == "menuPrincipal" && flagPedidosGuardados == true){
                llamarNewInstanceMenu(fragmentActual);
                setearToolbar(false,false,false,false,true, true);
            } else if (fragmentActual == "menuPrincipal" && flagPedidosCargados == true){
                llamarNewInstanceMenu(fragmentActual);
                setearToolbar(false,false,false,false,true, true);
            } else if (fragmentActual == "menuPrincipal" && flagNuevoPedido == true){
                llamarNewInstanceMenu(fragmentActual);
                setearToolbar(false,false,false,false,true, true);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        setearToolbar(false,false,false,false,true, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main2_action_listar_clientes_nuevos) {
            setearToolbar(false, true, false,false,false, false);
            llamarNewInstanceClientesNuevosCargados();
            return true;
        }
        if (id == R.id.main2_action_borrar_clientes_nuevos_cargados) {
            limpiarTablas("clientesNuevos");
            llamarNewInstanceClientesNuevosCargados();
            return true;
        }
        if (id == R.id.main2_action_listar_articulos_cargados) {
            setearToolbar(false, false, true, false, false,false);
            llamarNewInstanceArticulosCargados();
            return true;
        }
        if (id == R.id.main2_action_listar_clientes_cargados) {
            setearToolbar(false, false, false, true, false,false);
            llamarNewInstanceClientesCargados();
            return true;
        }
        if (id == R.id.main2_action_listar_pedidos_cargados) {
            setearToolbar(false, false, false, false,false,false);
            llamarNewInstancePedidosCargados();
            return true;
        }
        if (id == R.id.main2_action_listar_pedidos_guardados) {
            //setearToolbar(false, false, false, false,false,false);
            new ConfiguracionesDialogo(MainActivity.this, MainActivity.this, ordenarPor, ordenar);
            return true;
            /*
            llamarNewInstancePedidosGuardados();
            return true;
            */
        }

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

        if (id == R.id.nav_menu_principal) {
            if (fragmentActual != "menuPrincipal") {
                fragmentActual = "menuPrincipal";
                llamarNewInstanceMenu(fragmentActual);
            }
            setearToolbar(false,false,false,false,true, true);
        } else if (id == R.id.nav_nuevo_cliente) {
            if (fragmentActual != "nuevoCliente") {
                fragmentActual = "nuevoCliente";
                llamarNewInstanceNuevoCliente(fragmentActual, false, null);
            }
            setearToolbar(true, true, false,false,false,false);
        } else if (id == R.id.nav_importa_datos) {
            if (fragmentActual != "importarDatos") {
                fragmentActual = "importarDatos";
                llamarNewInstanceImportarDatos(fragmentActual);
            }
            setearToolbar(false,false,true,true,false,false);

        } else if (id == R.id.nav_salir) {
            setResult(RESULT_CANCELED);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void pedirPermisos() {
        // PERMISOS PARA ANDROID 6 O SUPERIOR
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    private void setearToolbar(boolean listarClientesNuevos, boolean borrarClientesNuevosCargados, boolean listarClientesCargados, boolean listarArticulosCargados, boolean listarPedidosCargados, boolean listarPedidosGuardados){
        toolbar.getMenu().findItem(R.id.main2_action_listar_clientes_nuevos).setVisible(listarClientesNuevos);
        toolbar.getMenu().findItem(R.id.main2_action_borrar_clientes_nuevos_cargados).setVisible(borrarClientesNuevosCargados);
        toolbar.getMenu().findItem(R.id.main2_action_listar_clientes_cargados).setVisible(listarClientesCargados);
        toolbar.getMenu().findItem(R.id.main2_action_listar_articulos_cargados).setVisible(listarArticulosCargados);
        toolbar.getMenu().findItem(R.id.main2_action_listar_pedidos_cargados).setVisible(listarPedidosCargados);
        toolbar.getMenu().findItem(R.id.main2_action_listar_pedidos_guardados).setVisible(listarPedidosGuardados);

    }

    private void setearFlags(boolean flagClientesNuevosCargados, boolean flagClientesCargados, boolean flagArticulosCargados, boolean flagNuevoPedido, boolean flagPedidosCargados, boolean flagPedidosGuardados) {
        this.flagClientesNuevosCargados =  flagClientesNuevosCargados;
        this.flagClientesCargados = flagClientesCargados;
        this.flagArticulosCargados = flagArticulosCargados;
        this.flagNuevoPedido = flagNuevoPedido;
        this.flagPedidosCargados = flagPedidosCargados;
        this.flagPedidosGuardados = flagPedidosGuardados;
    }

    private void llamarNewInstanceMenu(String fragmentActual){
        setearFlags(false,false,false,false,false, false);
        menuFragment = new MenuFragment();
        fragmentManager.beginTransaction().replace(R.id.contenedor, menuFragment).commit();
    }

    private void llamarNewInstanceImportarDatos(String fragmentActual){
        setearFlags(false, false, false, false, false, false);
        importarDatosFragment = new ImportarDatosFragment();
        fragmentManager.beginTransaction().replace(R.id.contenedor, importarDatosFragment).commit();
    }

    private void llamarNewInstanceNuevoCliente(String fragmentActual, boolean editar, ClienteNuevo clienteNuevo){
        setearFlags(false, false, false, false, false, false);
        nuevoClienteFragment = NuevoClienteFragment.newInstance(editar, clienteNuevo);
        fragmentManager.beginTransaction().replace(R.id.contenedor, nuevoClienteFragment).commit();
    }

    private void llamarNewInstanceCambiarPass(String fragmentActual){
        setearFlags(false, false, false, false, false, false);
        cambiarPassFragment = new CambiarPassFragment();
        fragmentManager.beginTransaction().replace(R.id.contenedor, cambiarPassFragment).commit();
    }

    private void llamarNewInstanceClientesCargados(){
        setearFlags(false, true, false, false, false, false);
        clientesCargadosFragment = new ClientesCargadosFragment();
        fragmentManager.beginTransaction().replace(R.id.contenedor, clientesCargadosFragment).commit();
    }

    private void llamarNewInstanceArticulosCargados(){
        setearFlags(false, false, true, false, false, false);
        articulosCargadosFragment = new ArticulosCargadosFragment();
        fragmentManager.beginTransaction().replace(R.id.contenedor, articulosCargadosFragment).commit();
    }

    private void llamarNewInstanceClientesNuevosCargados(){
        setearFlags(true, false, false, false, false, false);
        clientesNuevosCargadosFragment = new ClientesNuevosCargadosFragment();
        fragmentManager.beginTransaction().replace(R.id.contenedor, clientesNuevosCargadosFragment).commit();
    }

    private void llamarNewInstanceNuevoPedido(boolean editar, Pedido pedido, long codigoCliente, String nombreCliente){
        setearFlags(false, false, false, true, false, false);
        nuevoPedidoFragment = NuevoPedidoFragment.newInstance(editar, pedido, codigoCliente, nombreCliente);
        fragmentManager.beginTransaction().replace(R.id.contenedor, nuevoPedidoFragment).commit();
    }

    private void llamarNewInstancePedidosCargados(){
        setearFlags(false, false, false, false, true, false);
        pedidosCargadosFragment = new PedidosCargadosFragment("pedidosCargados", "", "");
        fragmentManager.beginTransaction().replace(R.id.contenedor, pedidosCargadosFragment).commit();
    }

    private void llamarNewInstancePedidosGuardados(String orden, String orderBy){
        setearFlags(false, false, false, false, false, true);
        pedidosCargadosFragment = new PedidosCargadosFragment("pedidosGuardados", orden, orderBy);
        fragmentManager.beginTransaction().replace(R.id.contenedor, pedidosCargadosFragment).commit();

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


    public void ResultadoNuevoCliente(ClienteNuevo clienteNuevo) {
        toolbar.getMenu().findItem(R.id.main2_action_listar_clientes_nuevos).setVisible(true);
        toolbar.getMenu().findItem(R.id.main2_action_borrar_clientes_nuevos_cargados).setVisible(true);
        llamarNewInstanceNuevoCliente(fragmentActual, true, clienteNuevo);
    }

    public void ResultadoBotoneraMainMenu(int accion, long codigoCliente, String nombreCliente) {
        if (accion == constantes.RESULT_NUEVO_PEDIDO) {
            this.codigoCliente = codigoCliente;
            this.nombreCliente = nombreCliente;

            toolbar.getMenu().findItem(R.id.main2_action_listar_pedidos_cargados).setVisible(false);
            toolbar.getMenu().findItem(R.id.main2_action_listar_pedidos_guardados).setVisible(false);
            llamarNewInstanceNuevoPedido(false, null, codigoCliente, nombreCliente);
        }
    }

    public void ResultadoPedido(Pedido pedido) {
        toolbar.getMenu().findItem(R.id.main2_action_listar_pedidos_cargados).setVisible(false);
        toolbar.getMenu().findItem(R.id.main2_action_listar_pedidos_guardados).setVisible(false);
        llamarNewInstanceNuevoPedido(true, pedido, codigoCliente, nombreCliente);
    }

    public void limpiarTablas(String tabla) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "dbSistema", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        admin.borrarRegistros(tabla, db);
    }

    @Override
    public void ResultadoConfiguracionesDialogo(String orden, String orderBy) {
        setearToolbar(false, false, false, false,false,false);
        switch (orden){
            case "codigo":
                this.ordenarPor = 1;
                break;
            case "nombre":
                this.ordenarPor = 2;
                break;
            case "fecha":
                this.ordenarPor = 3;
                break;
        }
        switch (orderBy){
            case "asc":
                this.ordenar = 1;
                break;
            case "desc":
                this.ordenar = 2;
                break;
        }
        llamarNewInstancePedidosGuardados(orden, orderBy);
    }
}
