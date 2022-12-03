package pe.edu.pucp.tdm.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;

public class ListaDispositivosActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    ArrayList<DispositivoDTO> listaDispositivo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);

        firebaseDatabase= FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.recyclerDispositivos);
        ListaDispositivosAdapter adapter = new ListaDispositivosAdapter();
        adapter.setContext(ListaDispositivosActivity.this);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("dispositivos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren() ){
                    DispositivoDTO actividad = children.getValue(DispositivoDTO.class);
                    listaDispositivo.add(actividad);
                    adapter.setListaDispositivos(listaDispositivo);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ListaDispositivosActivity.this));
                }
                if(listaDispositivo.size() == 0){
                    ((TextView) findViewById(R.id.textView)).setText("No hay dispositivos");
                }else{
                    ((TextView) findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.setEditar(new ListaDispositivosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(ListaDispositivosActivity.this,EditarDispositivoActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


        FloatingActionButton floatingActionButton = findViewById(R.id.floatingDispositivo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDispositivosActivity.this, AgregarDispositivoActivity.class);
                startActivity(intent);
            }
        });

    }
}