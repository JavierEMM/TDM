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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.admin.AdminMainActivity;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;
import pe.edu.pucp.tdm.login.LoginActivity;
import pe.edu.pucp.tdm.ti.ListaDispositivosActivity;
import pe.edu.pucp.tdm.ti.ListaDispositivosAdapter;

public class ClienteListaDispositivosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    ArrayList<DispositivoDTO> listaDispositivo = new ArrayList<>();
    ClienteListaDispositivosAdapter adapter = new ClienteListaDispositivosAdapter();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("dispositivos").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                listaDispositivo.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    DispositivoDTO dispositivoDTO = children.getValue(DispositivoDTO.class);
                    listaDispositivo.add(dispositivoDTO);
                }
                adapter.setListaDispositivos(listaDispositivo);
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_lista_dispositivos);

        firebaseDatabase= FirebaseDatabase.getInstance();
        SearchView searchView = findViewById(R.id.searchViewClienteDisp);
        searchView.setOnQueryTextListener(ClienteListaDispositivosActivity.this);
        RecyclerView recyclerView = findViewById(R.id.clienteRecycleDisp);
        adapter = new ClienteListaDispositivosAdapter();
        adapter.setContext(ClienteListaDispositivosActivity.this);
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
        getMenuInflater().inflate(R.menu.menu_cliente,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()) {
            case R.id.btnPedidos:
                Intent intent = new Intent(ClienteListaDispositivosActivity.this, ClienteListaPedidosActivity.class);
                startActivity(intent);
                return true;
            case R.id.btnCerrar:
                firebaseAuth.signOut();
                Intent intent2 =  new Intent(ClienteListaDispositivosActivity.this, LoginActivity.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);
        return false;
    }
}