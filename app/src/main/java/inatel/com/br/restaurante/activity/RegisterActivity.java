package inatel.com.br.restaurante.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import inatel.com.br.restaurante.R;
import inatel.com.br.restaurante.controller.SharedPreferencesController;
import inatel.com.br.restaurante.model.User;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegisterButton;

    private EditText mName;
    private EditText mPhoneNumber;
    private EditText mEmail;
    private EditText mPassword;

    private String mTextName;
    private String mTextPhoneNumber;
    private String mTextEmail;
    private String mTextPassword;

    private RelativeLayout mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //Pega instância do BANCO DE DADOS
    private DatabaseReference userReference = database.getReference("Usuarios"); //Cria referência da instância do banco para armazenar Usuários

    @Override
    public void onBackPressed() { //Se apertar o botão de voltar do Android, volta para tela de Login

        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() { //Pega referência do atual usuário logado
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mName = (EditText) findViewById(R.id.completeName);
        mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mRegisterButton = (Button) findViewById(R.id.registerButton);

        mProgressBar = (RelativeLayout) findViewById(R.id.progressBar);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mProgressBar.setVisibility(View.VISIBLE);

                //Pega Strings dos campos

                mTextName = mName.getText().toString();
                mTextPhoneNumber = mPhoneNumber.getText().toString();
                mTextEmail = mEmail.getText().toString();
                mTextPassword = mPassword.getText().toString();

                //Se estiverem vazias

                if ((mTextName.equals("") || mTextPhoneNumber.equals("") || mTextEmail.equals("") ||
                        mTextPassword.equals(""))) {
                    Toast.makeText(getApplicationContext(), "Alguns campos estão vazios.", Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                } else { //Senão..

                    //Função que cria usuário no Firebase utilizando o Email e a senha do mesmo

                    mAuth.createUserWithEmailAndPassword(mTextEmail, mTextPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                //Se falhar por algum motivo, mostra texto rápido na tela informando falha
                                Toast.makeText(RegisterActivity.this, "Algo falhou.", Toast.LENGTH_SHORT);
                            }

                            else {

                                //Se não, informa sucesso, esconde barra de loading, cria objeto Usuário
                                //de acordo com o construtor
                                Toast.makeText(RegisterActivity.this, "Sucesso!", Toast.LENGTH_SHORT);
                                mProgressBar.setVisibility(View.GONE);

                                User user = new User(mTextEmail, mTextPhoneNumber);

                                //Insere dentro da referência do banco o objeto Usuários
                                userReference.child(mTextName).setValue(user);

                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);

                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}

