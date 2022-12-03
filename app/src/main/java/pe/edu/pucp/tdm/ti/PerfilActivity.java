package pe.edu.pucp.tdm.ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.login.LoginActivity;

public class PerfilActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
        setContentView(R.layout.activity_perfil);

        drawerLayout = findViewById(R.id.drawer_layout_ti);
        navigationView = findViewById(R.id.nav_view_ti);
        actionBarDrawerToggle = new ActionBarDrawerToggle(PerfilActivity.this,drawerLayout,R.string.open,R.string.close);
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
                        Toast.makeText(PerfilActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(PerfilActivity.this, ListaDispositivosActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnPedidos:
                        Toast.makeText(PerfilActivity.this, "Pedidos", Toast.LENGTH_SHORT).show();
                        Intent intent1 =  new Intent(PerfilActivity.this, PedidosActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.btnPerfil:
                        Toast.makeText(PerfilActivity.this, "Ver Perfil", Toast.LENGTH_SHORT).show();
                        Intent intent2 =  new Intent(PerfilActivity.this, PerfilActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.btnCerrar:
                        Toast.makeText(PerfilActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent3 =  new Intent(PerfilActivity.this, LoginActivity.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
                return false;
            }
        });

        Button btnEdit = findViewById(R.id.btnEditFoto);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(PerfilActivity.this, EditFotoActivity.class);
                startActivity(intent);
            }
        });
    }
}