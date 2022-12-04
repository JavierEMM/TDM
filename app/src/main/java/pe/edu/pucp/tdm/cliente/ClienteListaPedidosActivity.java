package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaClientePedidosAdapter;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class ClienteListaPedidosActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    ArrayList<PedidoDTO> listaPedidos = new ArrayList<PedidoDTO>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista_pedidos);

        firebaseDatabase= FirebaseDatabase.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerPedidos);
        ListaClientePedidosAdapter adapter = new ListaClientePedidosAdapter();
        adapter.setContext(ClienteListaPedidosActivity.this);

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



    }
}