package ofa.cursos.android.app01.myresto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ofa.cursos.android.app01.myresto.modelo.Pedido;
import ofa.cursos.android.app01.myresto.modelo.PedidoDAOMemory;
import ofa.cursos.android.app01.myresto.modelo.PedidoDao;

public class ListaPedidosActivity extends AppCompatActivity {

    private PedidoDao pedidoDao;
    private ArrayAdapter<Pedido> adaptadorLista;
    Button btnNuevoPedido;
    ListView listaPedidos;

    private PedidoAdapter adaptadorPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        this.pedidoDao = new PedidoDAOMemory();
        this.btnNuevoPedido = (Button) findViewById(R.id.btnNuevosPedidos);
        this.listaPedidos = (ListView) findViewById(R.id.listaPedidos);
        //this.adaptadorLista = new ArrayAdapter<Pedido>(ListaPedidosActivity.this, android.R.layout.simple_list_item_1, pedidoDao.listarTodos());
        //this.listaPedidos.setAdapter(this.adaptadorLista);
        this.adaptadorPedido = new PedidoAdapter(this, pedidoDao.listarTodos());
        this.listaPedidos.setAdapter(this.adaptadorPedido);


        this.btnNuevoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cuando se hace click se debe iniciar la actividad MainActivity
                finish();
            }
        });

        this.listaPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                Pedido itemValue = (Pedido) listaPedidos.getItemAtPosition(position);
// ----------------------------------------------------------------
// ----------- COMPLETAR POR EL ALUMNO ----------------------------
// AGREGAR CODIGO PARA QUE PEDIDO DAO BORRE EL ELEMENTO DE LA LISTA
// ----------------------------------------------------------------
                pedidoDao.eliminar(itemValue);
                Toast.makeText(getApplicationContext(),
                        "Borrar elemento de posicion :" + itemPosition +
                                " Id: " + itemValue.getId() +
                                " nombre: " + itemValue.getNombre(), Toast.LENGTH_LONG).show();
                adaptadorLista.notifyDataSetChanged();
                return false;
            }
        });
        this.createNotificationChannel();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "11",
                    "canal1",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("descipcion");
            NotificationManager notificationManager
                    = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
