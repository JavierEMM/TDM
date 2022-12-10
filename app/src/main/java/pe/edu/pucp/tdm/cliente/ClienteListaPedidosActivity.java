package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaClientePedidosAdapter;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ClienteListaPedidosActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    ArrayList<PedidoDTO> listaPedidos = new ArrayList<PedidoDTO>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ListaClientePedidosAdapter adapter = new ListaClientePedidosAdapter();
    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("pedidos").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                listaPedidos.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()){
                    PedidoDTO pedidoDTO = children.getValue(PedidoDTO.class);
                    listaPedidos.add(pedidoDTO);
                }
                adapter.setListaPedidos(listaPedidos);
                adapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista_pedidos);

        firebaseDatabase= FirebaseDatabase.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerPedidos);
        ListaClientePedidosAdapter adapter = new ListaClientePedidosAdapter();
        adapter.setContext(ClienteListaPedidosActivity.this);
        final List<String> estado = Arrays.asList("Todos","Pendiente","Rechazado","Aceptado");
        Spinner spinner = findViewById(R.id.spinnerPedidos);
        ArrayAdapter adapterSpinner = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,estado);
        spinner.setAdapter(adapterSpinner);
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("pedidos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren() ){
                    PedidoDTO pedido = children.getValue(PedidoDTO.class);
                    Log.d("msg",pedido.getUserID());
                    Log.d("msg",FirebaseAuth.getInstance().getUid());
                    if(pedido.getUserID().equals(FirebaseAuth.getInstance().getUid())){
                        listaPedidos.add(pedido);
                        adapter.setListaPedidos(listaPedidos);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ClienteListaPedidosActivity.this));
                    }
                }
                if(listaPedidos.size() == 0){
                    ((TextView) findViewById(R.id.textView2)).setText("No hay pedidos");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button button = findViewById(R.id.buttonFiltro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = findViewById(R.id.spinnerPedidos);
                String filtro = spinner.getSelectedItem().toString();
                adapter.filtrado(filtro);
                Log.d("msg",filtro);

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_cliente,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case R.id.btnListarDispositivos:
                Intent intent = new Intent(ClienteListaPedidosActivity.this, ClienteListaDispositivosActivity.class);
                startActivity(intent);
                return true;
        }
     return super.onOptionsItemSelected(item);
    }
}