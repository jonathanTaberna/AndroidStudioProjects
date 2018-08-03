package ofa.cursos.android.app01.myresto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ofa.cursos.android.app01.myresto.modelo.Pedido;
import ofa.cursos.android.app01.myresto.modelo.PedidoDAOMemory;
import ofa.cursos.android.app01.myresto.modelo.PedidoDao;

public class MainActivity extends AppCompatActivity {

    private Button btnConfirmar;
    private EditText txtNombre;
    private Pedido pedidoActual;
    private PedidoDao pedidoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedidoActual = new Pedido();
        pedidoDao = new PedidoDAOMemory();
        txtNombre = (EditText) findViewById(R.id.txtNombreCliente);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmar);
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedidoActual.setNombre(txtNombre.getText().toString());
                Toast.makeText(MainActivity.this,"Pedido creado",Toast.LENGTH_LONG).show();
                Log.d("APP_MY_RESTO","Pedido confirmado!!!! ");
                Log.d("APP_MY_RESTO",pedidoActual.toString());
                // se agrega el pedido a la lista de pedidos
                pedidoDao.agregar(pedidoActual);
                // limpiar la variable para poder cargar un nuevo pedido
                pedidoActual = new Pedido();
                // limpiar el edit text en la pantalla
                txtNombre.setText("");
                Intent i = new Intent(MainActivity.this,ListaPedidosActivity.class);
                startActivity(i);
            }
        });
    }
}
