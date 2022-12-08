package pe.edu.pucp.tdm.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaDispositivosUserAdapter;
import pe.edu.pucp.tdm.adapters.ListaUsuariosAdapter;
import pe.edu.pucp.tdm.dto.PedidoDTO;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.dto.UsuarioDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class DetallesAdminActivity extends AppCompatActivity {


    ListaDispositivosUserAdapter adapter = new ListaDispositivosUserAdapter();
    ArrayList<PedidoDTO> pedidoDTOS = new ArrayList<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        UsuarioDTO usuarioDTO = intent.getSerializableExtra("usuario") == null ? null : (UsuarioDTO) intent.getSerializableExtra("usuario");
        if(usuarioDTO != null){
            Log.d("MENSAJE", "ENTRA AQUI");
            ((TextView) findViewById(R.id.textDispositivoUsuario)).setText("LISTA DISPOSITIVOS "+usuarioDTO.getCorreo());
            databaseReference.child("pedidos").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    pedidoDTOS.clear();
                    for(DataSnapshot children : dataSnapshot.getChildren()){
                        if(children.child("codigoPUCP").getValue(String.class).equals(usuarioDTO.getCodigoPUCP())){
                            Log.d("MENSAJE", "ENTRA AQUI2");
                            PedidoDTO pedidoDTO = children.getValue(PedidoDTO.class);
                            pedidoDTOS.add(pedidoDTO);
                        }
                    }
                    adapter.setList(pedidoDTOS);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_admin);


        //DRAWER
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View view =navigationView.getHeaderView(0);
        TextView nombre =  view.findViewById(R.id.nombreNav);
        TextView correo = view.findViewById(R.id.correoNav);

        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                TIUserDTO tiUserDTO =  dataSnapshot.getValue(TIUserDTO.class);
                nombre.setText(tiUserDTO.getNombres());
                correo.setText(tiUserDTO.getCorreo());
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(DetallesAdminActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("mensaje","ENTRA AQUí");
                switch (item.getItemId()){
                    case R.id.btnListarUsuariosTI:
                        Toast.makeText(DetallesAdminActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(DetallesAdminActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnReportes:
                        Toast.makeText(DetallesAdminActivity.this, "Reportes", Toast.LENGTH_SHORT).show();
                        Intent intent4 =  new Intent(DetallesAdminActivity.this, AdminReportesActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.btnLogOut:
                        Toast.makeText(DetallesAdminActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(DetallesAdminActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });


        //VISTA
        adapter.setContext(DetallesAdminActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recycleViewDispositivosUser);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetallesAdminActivity.this));
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}