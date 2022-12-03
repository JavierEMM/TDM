package pe.edu.pucp.tdm.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import pe.edu.pucp.tdm.login.LoginActivity;

public class ListaDispositivosActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ArrayList<DispositivoDTO> listaDispositivo = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);

        firebaseDatabase= FirebaseDatabase.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout_ti);
        navigationView = findViewById(R.id.nav_view_ti);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ListaDispositivosActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("mensaje","ENTRA AQUí");
                switch (item.getItemId()){
                    case R.id.btnListarDispositivos:
                        Toast.makeText(ListaDispositivosActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(ListaDispositivosActivity.this, ListaDispositivosActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnPedidos:
                        Toast.makeText(ListaDispositivosActivity.this, "Pedidos", Toast.LENGTH_SHORT).show();
                        Intent intent1 =  new Intent(ListaDispositivosActivity.this, PedidosActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.btnPerfil:
                        Toast.makeText(ListaDispositivosActivity.this, "Ver Perfil", Toast.LENGTH_SHORT).show();
                        Intent intent2 =  new Intent(ListaDispositivosActivity.this, PerfilActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.btnCerrar:
                        Toast.makeText(ListaDispositivosActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent3 =  new Intent(ListaDispositivosActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
                return false;
            }
        });

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
                databaseReference.child(adapter.getListaDispositivos().get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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