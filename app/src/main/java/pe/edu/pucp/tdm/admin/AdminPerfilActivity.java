package pe.edu.pucp.tdm.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaDispositivosUserAdapter;
import pe.edu.pucp.tdm.dto.PedidoDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class AdminPerfilActivity extends AppCompatActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_perfil);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(AdminPerfilActivity.this,drawerLayout,R.string.open,R.string.close);
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
                        Intent intent =  new Intent(AdminPerfilActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnReportes:
                        Intent intent4 =  new Intent(AdminPerfilActivity.this, AdminReportesActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.btnVerPerfil:
                        break;
                    case R.id.btnLogOut:
                        Toast.makeText(AdminPerfilActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(AdminPerfilActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
        ((Button) findViewById(R.id.btnEditarFotoPerfil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPerfilActivity.this,AdminEditarFotoPerfilActivity.class);
                startActivity(intent);
            }
        });
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