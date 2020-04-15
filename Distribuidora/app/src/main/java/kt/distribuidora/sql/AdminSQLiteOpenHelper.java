package kt.distribuidora.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private String dropTablaPedidosRestaurados = "DROP TABLE IF EXISTS pedidosRestaurados";
    private String createTablaPedidosRestaurados = "CREATE TABLE IF NOT EXISTS pedidosRestaurados(" +
                                                    "idPedido INTEGER PRIMARY KEY," +
                                                    "fechaRestauracion INTEGER" +
                                                    ")";

    private String dropTablaPedidosGuardados = "DROP TABLE IF EXISTS pedidosGuardados";
    private String createTablaPedidosGuardados = "CREATE TABLE IF NOT EXISTS pedidosGuardados(" +
                                                    "idPedido INTEGER PRIMARY KEY," +
                                                    "codigoVendedor LONG," +
                                                    "nombreVendedor TEXT," +
                                                    "codigoCliente LONG," +
                                                    "nombreCliente TEXT," +
                                                    "codigoListaPrecios INTEGER," +
                                                    "fechaPedido INTEGER," +
                                                    "comentariosPedido TEXT" +
                                                    ")";
    private String dropTablaProductosPedidosGuardados = "DROP TABLE IF EXISTS productosPedidosGuardados";
    private String createTablaProductosPedidosGuardados = "CREATE TABLE IF NOT EXISTS productosPedidosGuardados("+
                                                        "idPedido INTEGER," +
                                                        "codigoProducto INTEGER," +
                                                        "nombreProducto TEXT," +
                                                        "cantidadProducto INTEGER," +
                                                        "cantidadProductoBonif INTEGER," +
                                                        "precioProducto DECIMAL(10,2)," +
                                                        "PRIMARY KEY (idPedido, codigoProducto)" +
                                                        ")";


    private String dropTablaPedidos = "DROP TABLE IF EXISTS pedidos";
    private String createTablaPedidos = "CREATE TABLE IF NOT EXISTS pedidos("+
                                        "idPedido INTEGER PRIMARY KEY," + //" AUTOINCREMENT," +
                                        "codigoVendedor LONG," +
                                        "nombreVendedor TEXT," +
                                        "codigoCliente LONG," +
                                        "nombreCliente TEXT," +
                                        "codigoListaPrecios INTEGER," +
                                        "fechaPedido INTEGER," +
                                        "comentariosPedido TEXT" +
                                        ")";

    private String dropTablaProductosPedidos = "DROP TABLE IF EXISTS productosPedidos";
    private String createTablaProductosPedidos = "CREATE TABLE IF NOT EXISTS productosPedidos("+
                                                "idPedido INTEGER," +
                                                "codigoProducto INTEGER," +
                                                "nombreProducto TEXT," +
                                                "cantidadProducto INTEGER," +
                                                "cantidadProductoBonif INTEGER," +
                                                "precioProducto DECIMAL(10,2)," +
                                                "PRIMARY KEY (idPedido, codigoProducto)" +
                                                ")";

    private String dropTablaCategorias = "DROP TABLE IF EXISTS categorias";
    private String createTablaCategorias = "CREATE TABLE IF NOT EXISTS categorias(" +
                                            "codigo INTEGER PRIMARY KEY," +
                                            "descripcion TEXT" +
                                            ")";

    private String dropTablaClientesNuevos = "DROP TABLE IF EXISTS clientesNuevos";
    private String createTablaClientesNuevos = "CREATE TABLE IF NOT EXISTS clientesNuevos(" +
                                                "dni LONG PRIMARY KEY," +
                                                "categoria INTEGER," +
                                                "nombre TEXT," +
                                                "correo TEXT," +
                                                "telefono TEXT," +
                                                "direccion TEXT," +
                                                "codpos INTEGER," +
                                                "localidad TEXT" +
                                                ")";

    private String dropTablaClientes = "DROP TABLE IF EXISTS clientes";
    private String createTablaClientes = "CREATE TABLE IF NOT EXISTS clientes(" +
                                    "codigo INTEGER," +
                                    "nombre TEXT," +
                                    "codigoLista INTEGER," +
                                    "costo DECIMAL(10,2)," +
                                    "facturaConLista BOOLEAN," +
                                    "PRIMARY KEY (codigo, codigoLista)" +
                                    ")";

    private String dropTablaArticulos = "DROP TABLE IF EXISTS articulos";
    private String createTablaArticulos = "CREATE TABLE IF NOT EXISTS articulos(" +
                                    "codigo INTEGER," +
                                    "descripcion TEXT," +
                                    "costo DECIMAL(10,2)," +
                                    "codigoLista INTEGER," +
                                    "precio DECIMAL(10,2)," +
                                    "PRIMARY KEY (codigo, codigoLista)" +
                                    ")";

    private String dropTablaVendedores = "DROP TABLE IF EXISTS vendedores";
    private String createTablaVendedores = "CREATE TABLE IF NOT EXISTS vendedores(" +
                                    "codigo LONG PRIMARY KEY," +
                                    "nombre TEXT," +
                                    "password TEXT," +
                                    "ultimoPedidoGuardado INTEGER"+
                                    ")";


    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTablaPedidosRestaurados);
        db.execSQL(createTablaProductosPedidosGuardados);
        db.execSQL(createTablaPedidosGuardados);
        db.execSQL(createTablaProductosPedidos);
        db.execSQL(createTablaPedidos);
        db.execSQL(createTablaClientesNuevos);
        db.execSQL(createTablaClientes);
        db.execSQL(createTablaArticulos);
        db.execSQL(createTablaVendedores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void crearTabla(String tabla, SQLiteDatabase db) {
        if (tabla.equals("pedidosRestaurados")) {
            db.execSQL(createTablaPedidosRestaurados);
        } else if (tabla.equals("productosPedidosGuardados")) {
            db.execSQL(createTablaProductosPedidosGuardados);
        } else if (tabla.equals("pedidosGuardados")) {
            db.execSQL(createTablaPedidosGuardados);
        } else if (tabla.equals("productosPedidos")) {
            db.execSQL(createTablaProductosPedidos);
        } else if (tabla.equals("pedidos")) {
            db.execSQL(createTablaPedidos);
        } else if (tabla.equals("categorias")) {
            db.execSQL(createTablaCategorias);
        } else if (tabla.equals("clientesNuevos")) {
            db.execSQL(createTablaClientesNuevos);
        } else if (tabla.equals("clientes")) {
            db.execSQL(createTablaClientes);
        } else if (tabla.equals("articulos")) {
            db.execSQL(createTablaArticulos);
        } else if (tabla.equals("vendedores")) {
            db.execSQL(createTablaVendedores);
        }
    }

    public void borrarRegistros(String tabla, SQLiteDatabase db) {
        //db.execSQL("DELETE FROM "+tabla);
        switch (tabla){
            case "pedidosRestaurados":
                db.execSQL(dropTablaPedidosRestaurados);
                db.execSQL(createTablaPedidosRestaurados);
                break;
            case "productosPedidosGuardados":
                    db.execSQL(dropTablaProductosPedidosGuardados);
                    db.execSQL(createTablaProductosPedidosGuardados);
                    break;
            case "pedidosGuardados":
                    db.execSQL(dropTablaPedidosGuardados);
                    db.execSQL(createTablaPedidosGuardados);
                    break;
            case "productosPedidos":
                    db.execSQL(dropTablaProductosPedidos);
                    db.execSQL(createTablaProductosPedidos);
                    break;
            case "pedidos":
                    db.execSQL(dropTablaPedidos);
                    db.execSQL(createTablaPedidos);
                    break;
            case "categorias":
                    db.execSQL(dropTablaCategorias);
                    db.execSQL(createTablaCategorias);
                    break;
            case "clientesNuevos":
                    db.execSQL(dropTablaClientesNuevos);
                    db.execSQL(createTablaClientesNuevos);
                    break;
            case "clientes":
                    db.execSQL(dropTablaClientes);
                    db.execSQL(createTablaClientes);
                    break;
            case "articulos":
                    db.execSQL(dropTablaArticulos);
                    db.execSQL(createTablaArticulos);
                    break;
            case "vendedores":
                    db.execSQL(dropTablaVendedores);
                    db.execSQL(createTablaVendedores);
                    break;
            default:
                break;
        }
        /*
        if (tabla.equals("productosPedidosGuardados")) {
            db.execSQL(dropTablaProductosPedidosGuardados);
            db.execSQL(createTablaProductosPedidosGuardados);
        } else if (tabla.equals("pedidosGuardados")) {
            db.execSQL(dropTablaPedidosGuardados);
            db.execSQL(createTablaPedidosGuardados);
        } else if (tabla.equals("productosPedidos")) {
            db.execSQL(dropTablaProductosPedidos);
            db.execSQL(createTablaProductosPedidos);
        } else if (tabla.equals("pedidos")) {
            db.execSQL(dropTablaPedidos);
            db.execSQL(createTablaPedidos);
        } else if (tabla.equals("categorias")) {
            db.execSQL(dropTablaCategorias);
            db.execSQL(createTablaCategorias);
        } else if (tabla.equals("clientesNuevos")) {
            db.execSQL(dropTablaClientesNuevos);
            db.execSQL(createTablaClientesNuevos);
        } else if (tabla.equals("clientes")) {
            db.execSQL(dropTablaClientes);
            db.execSQL(createTablaClientes);
        } else if (tabla.equals("articulos")) {
            db.execSQL(dropTablaArticulos);
            db.execSQL(createTablaArticulos);
        } else if (tabla.equals("vendedores")) {
            db.execSQL(dropTablaVendedores);
            db.execSQL(createTablaVendedores);
        }

        */
    }

    public void borrarTablas(SQLiteDatabase db) {

        db.execSQL(dropTablaPedidosRestaurados);
        db.execSQL(createTablaPedidosRestaurados);

        db.execSQL(dropTablaProductosPedidosGuardados);
        db.execSQL(createTablaProductosPedidosGuardados);

        db.execSQL(dropTablaPedidosGuardados);
        db.execSQL(createTablaPedidosGuardados);

        db.execSQL(dropTablaProductosPedidos);
        db.execSQL(createTablaProductosPedidos);

        db.execSQL(dropTablaPedidos);
        db.execSQL(createTablaPedidos);

        db.execSQL(dropTablaCategorias);
        db.execSQL(createTablaCategorias);

        db.execSQL(dropTablaClientesNuevos);
        db.execSQL(createTablaClientesNuevos);

        db.execSQL(dropTablaClientes);
        db.execSQL(createTablaClientes);

        db.execSQL(dropTablaArticulos);
        db.execSQL(createTablaArticulos);

        db.execSQL(dropTablaVendedores);
        db.execSQL(createTablaVendedores);
    }

    public void crearTablas(SQLiteDatabase db) {
        db.execSQL(createTablaPedidosRestaurados);
        db.execSQL(createTablaProductosPedidosGuardados);
        db.execSQL(createTablaPedidosGuardados);
        db.execSQL(createTablaProductosPedidos);
        db.execSQL(createTablaPedidos);
        db.execSQL(createTablaCategorias);
        db.execSQL(createTablaClientesNuevos);
        db.execSQL(createTablaClientes);
        db.execSQL(createTablaArticulos);
        db.execSQL(createTablaVendedores);

    }
}
