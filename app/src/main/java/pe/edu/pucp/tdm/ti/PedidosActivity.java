package pe.edu.pucp.tdm.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;

public class PedidosActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    ArrayList<PedidoDTO> listaPedidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        firebaseDatabase= FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerPedidos);
        PedidosActivityAdapter adapter = new PedidosActivityAdapter();
        adapter.setContext(PedidosActivity.this);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("pedidos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren() ){
                    PedidoDTO pedido = children.getValue(PedidoDTO.class);
                    listaPedidos.add(pedido);
                    adapter.setListaPedidos(listaPedidos);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PedidosActivity.this));
                }
                if(listaPedidos.size() == 0){
                    ((TextView) findViewById(R.id.textView)).setText("No hay pedidos");
                }else{
                    ((TextView) findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}