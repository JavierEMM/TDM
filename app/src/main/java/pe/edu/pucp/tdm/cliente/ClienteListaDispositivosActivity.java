package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.ti.ListaDispositivosActivity;
import pe.edu.pucp.tdm.ti.ListaDispositivosAdapter;

public class ClienteListaDispositivosActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    ArrayList<DispositivoDTO> listaDispositivo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista_dispositivos);


        firebaseDatabase= FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.clienteRecycleDisp);
        ClienteListaDispositivosAdapter adapter = new ClienteListaDispositivosAdapter();
        adapter.setContext(ClienteListaDispositivosActivity.this);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("dispositivos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren() ){
                    DispositivoDTO actividad = children.getValue(DispositivoDTO.class);
                    listaDispositivo.add(actividad);
                    adapter.setListaDispositivos(listaDispositivo);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ClienteListaDispositivosActivity.this));
                }
                if(listaDispositivo.size() == 0){
                    ((TextView) findViewById(R.id.textView18)).setText("No hay dispositivos");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Button button = findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteListaDispositivosActivity.this,ClienteListaPedidosActivity.class);
                startActivity(intent);
            }
        });

    }
}