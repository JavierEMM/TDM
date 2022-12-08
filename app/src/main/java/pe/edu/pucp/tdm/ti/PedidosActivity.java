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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.DispositivoDTO;
import pe.edu.pucp.tdm.dto.PedidoDTO;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class PedidosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ArrayList<PedidoDTO> listaPedidos = new ArrayList<>();
    PedidosActivityAdapter adapter;

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
        setContentView(R.layout.activity_pedidos);

        firebaseDatabase= FirebaseDatabase.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout_ti);
        navigationView = findViewById(R.id.nav_view_ti);

        View view =navigationView.getHeaderView(0);
        TextView nombre =  view.findViewById(R.id.nombreNav);
        TextView correo = view.findViewById(R.id.correoNav);

        firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                TIUserDTO tiUserDTO =  dataSnapshot.getValue(TIUserDTO.class);
                nombre.setText(tiUserDTO.getNombres());
                correo.setText(tiUserDTO.getCorreo());
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(PedidosActivity.this,drawerLayout,R.string.open,R.string.close);
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
                        Toast.makeText(PedidosActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(PedidosActivity.this, ListaDispositivosActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnPedidos:
                        Toast.makeText(PedidosActivity.this, "Pedidos", Toast.LENGTH_SHORT).show();
                        Intent intent1 =  new Intent(PedidosActivity.this, PedidosActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.btnPerfil:
                        Toast.makeText(PedidosActivity.this, "Ver Perfil", Toast.LENGTH_SHORT).show();
                        Intent intent2 =  new Intent(PedidosActivity.this, PerfilActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.btnCerrar:
                        Toast.makeText(PedidosActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent3 =  new Intent(PedidosActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerPedidos);
        adapter = new PedidosActivityAdapter();
        adapter.setContext(PedidosActivity.this);

        SearchView searchView = findViewById(R.id.searchTextPedidos);
        searchView.setOnQueryTextListener(PedidosActivity.this);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("pedidos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot children : snapshot.getChildren() ){
                    PedidoDTO pedido = children.getValue(PedidoDTO.class);
                    pedido.setId(children.getKey());
                    listaPedidos.add(pedido);
                    adapter.setListaPedidos(listaPedidos);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PedidosActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.setAprobar(new PedidosActivityAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent =  new Intent(PedidosActivity.this, UbicacionActivity.class);
                intent.putExtra("pedido", adapter.getListaPedidos().get(position));
                startActivity(intent);
            }
        });
        adapter.setRechazar(new PedidosActivityAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent =  new Intent(PedidosActivity.this, RechazoActivity.class);
                intent.putExtra("pedido", adapter.getListaPedidos().get(position));
                startActivity(intent);
            }
        });

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