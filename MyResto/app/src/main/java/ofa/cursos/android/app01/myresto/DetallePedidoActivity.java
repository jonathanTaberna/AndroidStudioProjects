package ofa.cursos.android.app01.myresto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ofa.cursos.android.app01.myresto.modelo.ProductoDAO;
import ofa.cursos.android.app01.myresto.modelo.ProductoDaoMemory;
import ofa.cursos.android.app01.myresto.modelo.ProductoMenu;

public class DetallePedidoActivity extends AppCompatActivity {
    private ProductoDAO productoDao;
    private ArrayAdapter<ProductoMenu> adaptadorLista;
    ListView listaProductos;
    private ProductoMenu productoElegido;
    private Integer cantidadProducto;
    private TextView txtCantidad;
    private Button btnMenosProducto;
    private Button btnMasProducto;
    private Button btnAgregarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);
        productoDao = new ProductoDaoMemory();
        String[] listaProductos =
                getResources().getStringArray(R.array.listaProductos);
        this.listaProductos = (ListView) findViewById(R.id.listaProductos);
        productoDao.cargarDatos(listaProductos);
        adaptadorLista = new ArrayAdapter<ProductoMenu>(DetallePedidoActivity.this,
                android.R.layout.simple_list_item_single_choice, this.productoDao.listarMenu());
        this.listaProductos.setAdapter(adaptadorLista);
        this.listaProductos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        this.listaProductos.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                    View view, int position, long id) {
                productoElegido = adaptadorLista.getItem(position);
            }
        }
        );

        cantidadProducto = 0;
        btnMasProducto = (Button)findViewById(R.id.btnMasProducto);
        btnMenosProducto = (Button)findViewById(R.id.btnMenosProducto);
        btnAgregarProducto = (Button)findViewById(R.id.btnAgregarProducto);
        btnAgregarProducto.setEnabled(false);
        txtCantidad =(TextView)findViewById(R.id.detPedProductoCantidad);
        txtCantidad.setText(cantidadProducto.toString());
        btnMenosProducto.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cantidadProducto > 0) {
                        // si cantidadProducto es mayor que CERO (0)
                        // decrementar cantidadProducto
                        // asingar a txtCantidad el valor cantidadProducto
                        --cantidadProducto;
                        txtCantidad.setText(cantidadProducto.toString());
                    }
                    if (cantidadProducto <= 0) {
                        btnAgregarProducto.setEnabled(false);
                    }
                }
            }
        );
        btnMasProducto.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // incrementar cantidadProducto
                    // asingar a txtCantidad el valor cantidadProducto
                    ++cantidadProducto;
                    txtCantidad.setText(cantidadProducto.toString());
                    btnAgregarProducto.setEnabled(true);
                }
            }
        );
        btnAgregarProducto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cantidadProducto > 0) {
                            Intent intent = new Intent();
                            intent.putExtra("producto", productoElegido);
                            intent.putExtra("cantidad", cantidadProducto);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }
        );

    }
}
