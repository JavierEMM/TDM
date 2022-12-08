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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class AdminReportesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_admin_reportes);
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

        actionBarDrawerToggle = new ActionBarDrawerToggle(AdminReportesActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.btnListarUsuariosTI:
                        Toast.makeText(AdminReportesActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(AdminReportesActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnReportes:
                        break;
                    case R.id.btnLogOut:
                        Toast.makeText(AdminReportesActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(AdminReportesActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });


        ((Button) findViewById(R.id.btnListaUsuariosSistema)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminReportesActivity.this,ListaUsuariosActivity.class);
                startActivity(intent);
            }
        });
        ((Button) findViewById(R.id.btnEquiposPrestados)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminReportesActivity.this,AdminListaDispositivosPrestadosActivity.class);
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