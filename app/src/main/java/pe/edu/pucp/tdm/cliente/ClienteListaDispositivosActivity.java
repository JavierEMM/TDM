package pe.edu.pucp.tdm.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

        Button button = findViewById(R.id.button8);


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




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_ti,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case R.id.btnPedidos:
                Intent intent = new Intent(ClienteListaDispositivosActivity.this, ClienteListaPedidosActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}