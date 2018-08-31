package hsfarmacia.farmaclub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hsfarmacia.farmaclub.adaptador.AdaptadorProductos;
import hsfarmacia.farmaclub.provisorios.Productos;
import hsfarmacia.farmaclub.provisorios.ProductosVector;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private String tarjeta;
    private int puntos;
    private String nombre;


    private RecyclerView recyclerView;
    public AdaptadorProductos adaptador;
    private RecyclerView.LayoutManager layoutManager;

    //provisorios
    public static Productos productos = new ProductosVector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptador = new AdaptadorProductos(this, productos);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        //recyclerView.addItemDecoration(decoration);
        // iniciar Actividad para detalle del producto
        //adaptador.setOnItemClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent i = new Intent(MainActivity.this, VistaLugarActivity.class);
        //        i.putExtra("id", (long) recyclerView.getChildAdapterPosition(v));
        //        startActivity(i);
        //    }
        //});



        Bundle extras = getIntent().getExtras();
        tarjeta = extras.getString("tarjeta");
        puntos = extras.getInt("puntos");
        nombre = extras.getString("nombre");

        TextView tvTarjeta = (TextView) findViewById(R.id.tvTarjeta);
        TextView tvPuntos = (TextView) findViewById(R.id.tvPuntos);
        TextView tvNombre = (TextView) findViewById(R.id.tvNombre);

        tvTarjeta.setText("El codigo de tarjeta es: " + tarjeta);
        tvPuntos.setText("Los puntos disponibles son: " + puntos);
        tvNombre.setText("Bienvenido " + nombre);

    }

}
