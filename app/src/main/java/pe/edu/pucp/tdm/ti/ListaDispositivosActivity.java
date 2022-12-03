package pe.edu.pucp.tdm.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.admin.AdminMainActivity;
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
                    actividad.setId(children.getKey());
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
                intent.putExtra("dispositivo",adapter.getListaDispositivos().get(position));
                startActivity(intent);
            }
        });
        adapter.setBorrar(new ListaDispositivosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                DispositivoDTO d = adapter.getListaDispositivos().get(position);
                databaseReference.child("dispositivos").child(d.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ListaDispositivosActivity.this,"Eliminado correctamente",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ListaDispositivosActivity.this,"Error al eliminar",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                adapter.getListaDispositivos().remove(position);
                adapter.notifyItemRemoved(position);
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