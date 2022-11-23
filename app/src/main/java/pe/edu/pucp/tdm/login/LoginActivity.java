package pe.edu.pucp.tdm.login;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pe.edu.pucp.tdm.R;

public class LoginActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        }else{
            ((Button) findViewById(R.id.btnRegister)).setOnClickListener(view -> {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            });
            ((Button) findViewById(R.id.btnLogin)).setOnClickListener(view -> {
                EditText email = findViewById(R.id.editEmailLogin);
                EditText contrasena = findViewById(R.id.editPasswordLogin);
                //
                boolean bool=true;
                if(email.getText().toString().isEmpty()){
                    email.setError("Ingrese un email");
                    bool =false;
                }
                if(contrasena.getText().toString().isEmpty()){
                    contrasena.setError("Ingrese una contrasena");
                    bool =false;
                }
                if(bool){
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),contrasena.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                    Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Confirme su correo electronico", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "Verifique sus credenciales", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        ((TextView) findViewById(R.id.textForgot)).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}