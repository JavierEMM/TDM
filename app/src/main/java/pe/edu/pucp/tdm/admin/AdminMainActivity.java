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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
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

import java.util.ArrayList;

import pe.edu.pucp.tdm.R;
import pe.edu.pucp.tdm.adapters.ListaTIAdapter;
import pe.edu.pucp.tdm.dto.TIUserDTO;
import pe.edu.pucp.tdm.login.LoginActivity;
import pe.edu.pucp.tdm.ti.ListaDispositivosActivity;

public class AdminMainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListaTIAdapter adapter =  new ListaTIAdapter();
    ArrayList<TIUserDTO> tiUserDTOS = new ArrayList<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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
    protected void onResume() {
        super.onResume();
        databaseReference.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                tiUserDTOS.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()){
                    if(children.child("rol").getValue(String.class).equals("ROL_TI")){
                        TIUserDTO tiUserDTO = children.getValue(TIUserDTO.class);
                        tiUserDTOS.add(tiUserDTO);
                    }
                }
                adapter.setList(tiUserDTOS);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        //FIREBASE
        //DRAWER
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

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

        actionBarDrawerToggle = new ActionBarDrawerToggle(AdminMainActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("mensaje","ENTRA AQU??");
                switch (item.getItemId()){
                    case R.id.btnListarUsuariosTI:
                        break;
                    case R.id.btnReportes:
                        Intent intent4 =  new Intent(AdminMainActivity.this, AdminReportesActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.btnLogOut:
                        firebaseAuth.signOut();
                        Intent intent2 =  new Intent(AdminMainActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
        SearchView searchView = findViewById(R.id.searchTextUsuarios);
        searchView.setOnQueryTextListener(AdminMainActivity.this);
        //VISTA
        adapter.setContext(AdminMainActivity.this);
        RecyclerView recyclerView = findViewById(R.id.recycleViewTI);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminMainActivity.this));
        adapter.setBorrar(new ListaTIAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                TIUserDTO tiUserDTO2 = adapter.getList().get(position);
                databaseReference.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for(DataSnapshot children : dataSnapshot.getChildren()){
                            if(children.child("rol").getValue(String.class).equals("ROL_TI")){
                                TIUserDTO tiUserDTO = children.getValue(TIUserDTO.class);
                                if(tiUserDTO.getDni().equals(tiUserDTO2.getDni())) {
                                    databaseReference.child("users").child(children.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AdminMainActivity.this,"Eliminado correctamente",Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(AdminMainActivity.this,"Error al eliminar",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
                adapter.getList().remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        adapter.setEditar(new ListaTIAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(AdminMainActivity.this, EditarTIActivity.class);
                intent.putExtra("ti",adapter.getList().get(position));
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.btnAgregarTIAdminLista)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,AgregarTIActivity.class);
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