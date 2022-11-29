package pe.edu.pucp.tdm.admin;

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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaUsuariosAdapter;
import pe.edu.pucp.tdm.dto.UsuarioDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class ListaUsuariosActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth firebaseAuth;

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
        setContentView(R.layout.activity_lista_usuarios_admin);
        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();
        //DRAWER
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ListaUsuariosActivity.this,drawerLayout,R.string.open,R.string.close);
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
                        Toast.makeText(ListaUsuariosActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnReportes:
                        Toast.makeText(ListaUsuariosActivity.this, "Reportes", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnVerPerfil:
                        Toast.makeText(ListaUsuariosActivity.this, "Ver Perfil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnLogOut:
                        Toast.makeText(ListaUsuariosActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent =  new Intent(ListaUsuariosActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });
        ArrayList<UsuarioDTO> list = new ArrayList<>();
        UsuarioDTO userDTO = new UsuarioDTO();
        userDTO.setCorreo("Juan Carlos");
        userDTO.setDNI("74229427");
        list.add(userDTO);
        //VISTA
        ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
        adapter.setList(list);
        adapter.setContext(ListaUsuariosActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recycleViewUsuarios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListaUsuariosActivity.this));
        adapter.setDetalles(new ListaUsuariosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(ListaUsuariosActivity.this,DetallesAdminActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }
}