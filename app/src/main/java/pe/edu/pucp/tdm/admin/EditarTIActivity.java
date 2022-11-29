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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.login.LoginActivity;

public class EditarTIActivity extends AppCompatActivity {

    TIUserDTO tiUserDTO = new TIUserDTO();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

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
        setContentView(R.layout.activity_editar_tiactivity);
        //Obtengo el Usuario
        Intent intent = getIntent();
        TIUserDTO tiUserDTO = (TIUserDTO) intent.getSerializableExtra("ti");
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        EditText nombres = findViewById(R.id.editTextNombre);
        nombres.setText(tiUserDTO.getNombres());
        EditText apellidos = findViewById(R.id.editTextApellido);
        apellidos.setText(tiUserDTO.getApellidos());
        EditText dni = findViewById(R.id.editTextDNI);
        dni.setText(tiUserDTO.getDni());
        dni.setEnabled(false);
        //DRAWER
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(EditarTIActivity.this,drawerLayout,R.string.open,R.string.close);
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
                        Toast.makeText(EditarTIActivity.this, "Listar", Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(EditarTIActivity.this, ListaUsuariosActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btnReportes:
                        Toast.makeText(EditarTIActivity.this, "Reportes", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnVerPerfil:
                        Toast.makeText(EditarTIActivity.this, "Ver Perfil", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnLogOut:
                        Toast.makeText(EditarTIActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(EditarTIActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
        ((Button) findViewById(R.id.btnAceptarEditarTI)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            databaseReference.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children :dataSnapshot.getChildren() ){
                        if(children.child("rol").getValue(String.class).equals("ROL_TI")){
                            TIUserDTO tiUserDTO1 = children.getValue(TIUserDTO.class);
                            if(tiUserDTO.getDni().equals(tiUserDTO1.getDni())){
                                String nombreStr = nombres.getText().toString();
                                String apellidoStr = apellidos.getText().toString();
                                tiUserDTO.setNombres(nombreStr);
                                tiUserDTO.setApellidos(apellidoStr);
                                databaseReference.child("users").child(children.getKey()).setValue(tiUserDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent1 = new Intent(EditarTIActivity.this,AdminMainActivity.class);
                                            startActivity(intent1);
                                        }else{
                                            Toast.makeText(EditarTIActivity.this,"Error al editar",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
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